package dash.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import dash.commons.core.index.Index;
import dash.commons.util.StringUtil;
import dash.logic.parser.exceptions.ParseException;
import dash.model.person.Address;
import dash.model.person.Email;
import dash.model.person.Name;
import dash.model.person.Phone;
import dash.model.tag.Tag;
import dash.model.task.CompletionStatus;
import dash.model.task.TaskDate;
import dash.model.task.TaskDescription;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Address parseAddress(String address) {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses a {@code String description} into a {@code TaskDescription}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code description} is invalid.
     */
    public static TaskDescription parseTaskDescription(String description) throws ParseException {
        requireNonNull(description);
        String trimmedDescription = description.trim();
        if (!TaskDescription.isValidDescription(trimmedDescription)) {
            throw new ParseException(TaskDescription.MESSAGE_CONSTRAINTS);
        }
        return new TaskDescription(trimmedDescription);
    }

    /**
     * Parses a {@code String taskDate} into a {@code TaskDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code taskDate} is invalid.
     */
    public static TaskDate parseTaskDate(String taskDate) throws ParseException {
        requireNonNull(taskDate);
        String trimmedTaskDate = taskDate.trim();
        if (!TaskDate.isValidTaskDate(trimmedTaskDate)) {
            throw new ParseException(TaskDate.MESSAGE_CONSTRAINTS);
        }

        return new TaskDate(trimmedTaskDate);
    }

    /**
     * Parses a {@code String taskDate} into a {@code TaskDate} specialised for editing a TaskDate.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code taskDate} is invalid.
     */
    public static TaskDate parseTaskDateToEdit(String taskDate) throws ParseException {
        requireNonNull(taskDate);
        String trimmedTaskDate = taskDate.trim();
        if (!TaskDate.isValidTaskDate(trimmedTaskDate)) {
            throw new ParseException(TaskDate.MESSAGE_CONSTRAINTS);
        }
        return new TaskDate(trimmedTaskDate, true);
    }

    /**
     * Parses a {@code String taskDate} into a {@code TaskDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code taskDate} is invalid.
     */
    public static CompletionStatus parseCompletionStatus(String completionStatus) throws ParseException {
        requireNonNull(completionStatus);
        String trimmedCompletionStatus = completionStatus.trim();
        int validStatus = CompletionStatus.isValidCompletionStatus(trimmedCompletionStatus);
        if (validStatus < 0) {
            throw new ParseException(CompletionStatus.MESSAGE_CONSTRAINTS);
        } else if (validStatus == 0) {
            return new CompletionStatus(true);
        } else if (validStatus == 1) {
            return new CompletionStatus(false);
        } else {
            return new CompletionStatus(false);
        }
    }

    /**
     * Parses {@code Collection<String> personIndices} into a {@code Set<Index>}.
     *
     * @throws ParseException if the any given index is invalid.
     */
    public static Set<Index> parsePersonIndex(Collection<String> personIndices) throws ParseException {
        requireNonNull(personIndices);
        final Set<Index> indexSet = new HashSet<>();
        for (String index : personIndices) {
            indexSet.add(parseIndex(index));
        }
        return indexSet;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
