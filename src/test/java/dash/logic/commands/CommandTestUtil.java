package dash.logic.commands;

import static dash.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static dash.logic.parser.CliSyntax.PREFIX_EMAIL;
import static dash.logic.parser.CliSyntax.PREFIX_NAME;
import static dash.logic.parser.CliSyntax.PREFIX_PHONE;
import static dash.logic.parser.CliSyntax.PREFIX_TAG;
import static dash.logic.parser.CliSyntax.PREFIX_TASK_DESCRIPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dash.commons.core.index.Index;
import dash.logic.commands.exceptions.CommandException;
import dash.logic.commands.personcommand.EditPersonCommand;
import dash.logic.commands.taskcommand.EditTaskCommand;
import dash.model.AddressBook;
import dash.model.Model;
import dash.model.person.NameContainsKeywordsPredicate;
import dash.model.person.Person;
import dash.model.task.DescriptionContainsKeywordsPredicate;
import dash.model.task.Task;
import dash.testutil.Assert;
import dash.testutil.EditPersonDescriptorBuilder;
import dash.testutil.EditTaskDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String VALID_TAG_PROJECT = "project";
    public static final String VALID_TAG_UNGRADED = "ungraded";
    public static final String VALID_TAG_QUIZ = "quiz";
    public static final String VALID_TAG_GROUPWORK = "groupwork";
    public static final String VALID_TAG_HOMEWORK = "homework";

    public static final String VALID_TASK_DESCRIPTION_TP = "Finish CS2103T tP";
    public static final String VALID_TASK_DESCRIPTION_TEST = "Study for CS2100 test";
    public static final String VALID_TASK_DESCRIPTION_ASSIGNMENT = "Submit CS2100 Assignment";
    public static final String VALID_TASK_DESCRIPTION_LECTURE = "Catch up with ST2334 lectures";
    public static final String VALID_TASK_DESCRIPTION_PR_REVIEW = "Do PR review";
    public static final String VALID_TASK_DESCRIPTION_QUIZ = "ST2334 quiz before Friday";

    public static final String VALID_TASK_DATE_ASSIGNMENT = "2359";
    public static final String VALID_TASK_DATE_LECTURE = "21/10/2021";
    public static final String VALID_TASK_DATE_PR_REVIEW = "21/02/2021, 10:00 PM";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String TAG_DESC_UNGRADED = " " + PREFIX_TAG + VALID_TAG_UNGRADED;
    public static final String TAG_DESC_QUIZ = " " + PREFIX_TAG + VALID_TAG_QUIZ;
    public static final String TAG_DESC_GROUPWORK = " " + PREFIX_TAG + VALID_TAG_GROUPWORK;
    public static final String TAG_DESC_HOMEWORK = " " + PREFIX_TAG + VALID_TAG_HOMEWORK;

    public static final String TASK_DESC_ASSIGNMENT = " " + PREFIX_TASK_DESCRIPTION + VALID_TASK_DESCRIPTION_ASSIGNMENT;
    public static final String TASK_DESC_LECTURE = " " + PREFIX_TASK_DESCRIPTION + VALID_TASK_DESCRIPTION_LECTURE;
    public static final String TASK_DESC_PR_REVIEW = " " + PREFIX_TASK_DESCRIPTION + VALID_TASK_DESCRIPTION_PR_REVIEW;
    public static final String TASK_DESC_QUIZ = " " + PREFIX_TASK_DESCRIPTION + VALID_TASK_DESCRIPTION_QUIZ;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_TASK_DESC = " " + PREFIX_TASK_DESCRIPTION + " "; // whitespace not allowed

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditPersonCommand.EditPersonDescriptor DESC_AMY;
    public static final EditPersonCommand.EditPersonDescriptor DESC_BOB;
    public static final EditTaskCommand.EditTaskDescriptor DESC_CS2103T_TP;
    public static final EditTaskCommand.EditTaskDescriptor DESC_CS2100_TEST;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        DESC_CS2103T_TP = new EditTaskDescriptorBuilder().withTaskDescription(VALID_TASK_DESCRIPTION_TP)
                .withTags(VALID_TAG_PROJECT).build();
        DESC_CS2100_TEST = new EditTaskDescriptorBuilder().withTaskDescription(VALID_TASK_DESCRIPTION_TEST)
                .withTags(VALID_TAG_UNGRADED).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        Assert.assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Updates {@code model}'s filtered task list to show only the task at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showTaskAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredTaskList().size());

        Task task = model.getFilteredTaskList().get(targetIndex.getZeroBased());
        final String[] splitDesc = task.getTaskDescription().toString().split("\\s+");
        model.updateFilteredTaskList(new DescriptionContainsKeywordsPredicate(Arrays.asList(splitDesc[0])));

        assertEquals(1, model.getFilteredTaskList().size());
    }
}
