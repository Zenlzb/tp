package dash.logic.parser.personcommand;

import static dash.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static dash.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static dash.logic.parser.CliSyntax.PREFIX_EMAIL;
import static dash.logic.parser.CliSyntax.PREFIX_NAME;
import static dash.logic.parser.CliSyntax.PREFIX_PHONE;
import static dash.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import dash.logic.commands.personcommand.AddPersonCommand;
import dash.logic.parser.ArgumentMultimap;
import dash.logic.parser.ArgumentTokenizer;
import dash.logic.parser.Parser;
import dash.logic.parser.ParserUtil;
import dash.logic.parser.Prefix;
import dash.logic.parser.exceptions.ParseException;
import dash.model.person.Address;
import dash.model.person.Email;
import dash.model.person.Name;
import dash.model.person.Person;
import dash.model.person.Phone;
import dash.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddPersonCommand object
 */
public class AddPersonCommandParser implements Parser<AddPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPersonCommand
     * and returns an AddPersonCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPersonCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (argMultimap.getValue(PREFIX_NAME).isEmpty()
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPersonCommand.MESSAGE_USAGE));
        }

        Email email;
        Address address;
        Phone phone;

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());

        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        } else {
            phone = new Phone();
        }

        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        } else {
            email = new Email();
        }

        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        } else {
            address = new Address();
        }

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person = new Person(name, phone, email, address, tagList);

        return new AddPersonCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
