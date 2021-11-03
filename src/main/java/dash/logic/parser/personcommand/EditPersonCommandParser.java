package dash.logic.parser.personcommand;

import static dash.commons.core.Messages.MESSAGE_ARGUMENT_EMPTY;
import static dash.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static dash.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static dash.logic.parser.CliSyntax.PREFIX_EMAIL;
import static dash.logic.parser.CliSyntax.PREFIX_NAME;
import static dash.logic.parser.CliSyntax.PREFIX_PHONE;
import static dash.logic.parser.CliSyntax.PREFIX_TAG;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import dash.commons.core.index.Index;
import dash.logic.commands.personcommand.EditPersonCommand;
import dash.logic.commands.personcommand.EditPersonCommand.EditPersonDescriptor;
import dash.logic.parser.ArgumentMultimap;
import dash.logic.parser.ArgumentTokenizer;
import dash.logic.parser.Parser;
import dash.logic.parser.ParserUtil;
import dash.logic.parser.exceptions.ParseException;
import dash.model.person.Address;
import dash.model.person.Email;
import dash.model.person.Phone;
import dash.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditPersonCommand object
 */
public class EditPersonCommandParser implements Parser<EditPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditPersonCommand
     * and returns an EditPersonCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditPersonCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditPersonCommand.MESSAGE_USAGE),
                    pe);
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            if (argMultimap.getValue(PREFIX_NAME).get().isEmpty()) {
                throw new ParseException(MESSAGE_ARGUMENT_EMPTY);
            }
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            if (argMultimap.getValue(PREFIX_PHONE).get().isEmpty()) {
                editPersonDescriptor.setPhone(new Phone());
            } else {
                editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
            }
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            if (argMultimap.getValue(PREFIX_EMAIL).get().isEmpty()) {
                editPersonDescriptor.setEmail(new Email());
            } else {
                editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
            }
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            if (argMultimap.getValue(PREFIX_ADDRESS).get().isEmpty()) {
                editPersonDescriptor.setAddress(new Address());
            } else {
                editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
            }
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditPersonCommand.MESSAGE_NOT_EDITED);
        }

        return new EditPersonCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
