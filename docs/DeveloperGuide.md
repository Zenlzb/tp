---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `TabMenu`, `StatusBarFooter` etc. 
The `TabMenu` component utilises JavaFx's TabPane to implement the tab system. The `TabMenu` is then broken up into 3 tabs, 
the contacts, tasks and help tab. 
The contacts tab comprises `PersonListPanel`.
The tasks tab comprises `TaskListPanel` and `PersonListPanelMinimal`.
The help tab comprises `HelpPanel`.
All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays the `Person` and `Task` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2122S1-CS2103T-W15-2/tp/blob/master/src/main/java/dash/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="diagrams/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the respective tab parser to parse the command
   - `ContactsTabParser` while on the contacts tab
   - `TaskTabParser` while on the tasks tab
   - `HelpTabParser` while on the help tab
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddPersonCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API 
call while on the contacts tab.

![Interactions Inside the Logic Component for the `delete 1` Command](diagrams/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteTaskCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="diagrams/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the relevant tab parser (e.g. `ContactsTabParser`) class creates an `XYZCommandParser` 
  (`XYZ` is a placeholder for the specific command name e.g., `AddTaskCommandParser`) which uses the other classes shown 
  above to parse the user command and create a `XYZCommand` object (e.g., `AddTaskCommand`) which the tab parser returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddTaskCommandParser`, `DeleteTaskCommandParser`, ...) inherit from the `Parser` 
  interface so that they can be treated similarly where possible e.g, during testing.
* Some parsers require the use of the filtered person list, and they extend `ParserRequiringPersonList` instead.

Here are the remaining classes in logic, mainly utility classes:  
![Parser Utility Classes](diagrams/ParserUtilityClasses.png)


### Model component
**API** : [`Model.java`](https://github.com/AY2122S1-CS2103T-W15-2/tp/blob/master/src/main/java/dash/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the contacts' data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the task list data i.e., all `Task` objects  (which are contained in a `TaskList` object).
* stores the currently 'selected' `Person` or `Task` objects (e.g., results of a search query) as separate _filtered_ lists which are exposed to outsiders as unmodifiable `ObservableList<Person>` or `ObservableList<Task>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Implemented\] Tabs System (Aaron)

#### Implementation
The tab system is implemented using the JavaFX `javafx.scene.control.Tab` class. It is
encapsulated in its own class `TabMenu` within the `dash.ui` package as a UI component.

Additionally, it expands the number of parsers from `ContactsTabParser` to have a `TasksTabParser` 
and `HelpTabParser` to allow for a different list of commands to be parsed per tab/page.

The following commands are implemented:
* `SwitchTabContactsCommand`
* `SwitchTabTasksCommand`
* `SwitchTabHelpCommand`

The following operations are implemented:
* `MainWindow#handleHelp()`
* `MainWindow#handleSwitchContactsTab()`
* `MainWindow#handleSwitchTasksTab()`
These operations handle the switching of tabs within the MainWindow class.

Here is an example of how the tab system works:

Step 1: The user launches the application. By default, the contacts
tab will be shown. 

Step 2: The user adds a contact by using the `add` command. The `LogicManager` object is passed the command and
parses the command using the `ContactsTabParser` since it is on the contacts page and hence uses the contacts parser.

Step 3: The user decides to switch to the task tab by entering the command: `task`. The command follows a similar
path like in Step 2.

Step 4: The commandResult object returned after execution indicates that the user wants to switch to the Task tab and 
the method `handleSwitchTasksTab()` is called.

Step 5: The method first calls the `switchTab()` method within `TabMenu`, which directs the UI 
to switch to the selected tab index (1 for the Tasks tab). Then, it calls `setTabNumber` within `LogicManager` so
the `LogicManager` can keep track of the current tab, and use 1 out of the 3 parsers to fit the tab the user is on.

Step 6: The user attempts to add a contact by using the same `add` command. However, since Step 5 has recorded the tab
number inside `LogicManager` to be 1, the `LogicManager` detects that the user is still on the Task tab and hence uses 
the `TaskTabParser` to parse the `add` command. 

Step 7: The `TaskTabParser` returns an `AddTaskCommand` instead of an `AddPersonCommand`.

The following sequence diagram shows how the switch tab system works when the command `task` is entered on the Contacts
Tab:
![TabSystemSequenceDiagram](images/TabSystemSequenceDiagram.png)

The following activity diagram summarises what happens when a user executes a switch tab command:
![TabSystemActivityDiagram](images/TabSystemActivityDiagram.png)

#### Design considerations:

**Aspect: How switching tabs execute:**

* **Alternative 1 (current choice):** Uses the JavaFX implementation of tabs.
    * Pros: Easy to implement.
    * Cons: Have to disable clicking of tabs because clicking in JavaFX has a different
            implementation to how the Logic component parses commands.

* **Alternative 2:** One single panel/list that changes/updates its displayed list depending on 
user choice.
    * Pros: Lesser dependence on JavaFX components.
    * Cons: Not very scalable in case more tabs are needed in the future, hard to implement multiple segments
            within a tab.

### \[Implemented\] Assigning People to Tasks (Zen)

We integrated the existing address book system of storing contacts with our new task list system by adding the ability
to assign people from your address book to tasks in your task list.   
Possible use cases:
- Assigning group members to group tasks to keep track of who to contact.
- Assigning people who have RSVPed to a gathering to keep track of who is attending.
- Assigning which prof to email after completing a piece of work.

#### Implementation
In the `Task.java` class, `people` is a Set of `Person` objects that represents a task being assigned people, similar to
`tags`. A user can assign people to a task in 2 ways:
- The `assign` command, which adds more `Person` objects to the existing `people` set.
- The `edit` command, which replaces the existing `people` set with a new one. 

The `Person` objects are assigned to a task using the relevant index currently being displayed on the filtered person 
list. All indices that represent people are preceded with the `p/` prefix. We added the side panel on the tasks tab 
so that it would be easier to see the list of contacts to assign people to tasks, instead of needing to switch tabs 
back and forth when assigning people.
Example usage of either command is as follows:
- `assign 2 p/1 p/3` - Assigns persons 1 and 3 to task 2.
- `edit 2 p/1 p/3` - Replaces task 2's set of people with a set consisting of persons 1 and 3.

A sequence diagram is provided below:
![AssignPeopleSequenceDiagram](images/AssignPeopleSequenceDiagram.png)

#### Challenges  

This was the first set of commands that required access to the address book, so implementing the parser for the 
commands simply by implementing the `Parser` interface was not sufficient. An extension to the `Parser`
interface, `ParserRequiringPersonList` was created for these commands specifically, and an alternative parse function
was created which took in the person list from the command parser `TaskTabParser`.

#### Alternatives Considered  

- Assigning people using their names instead of index

Pros: No need to remember indices  
Cons: Too much of a hassle with long names, and using only parts of a name was not feasible due to
the possibility of multiple people with the same first name, etc. The side panel was implemented to circumvent
this problem


### \[Implemented\] Adding Date/Time to Tasks (Myat)

To add onto our new task list system, just like how the address book allows storing of additional information like
phone number and email, the user can now add in a task's date with an optional time information. The addition of
date/time as a new type of information to be stored opens the door to more functionalities such as listing upcoming
tasks and finding tasks based on date/time.

Possible use cases:
- Adding date/time of a task that is due at the specified date and time.
- Adding date/time of a task that the user wants to start working on at the specified date and time.

#### Implementation

The `TaskDate.java` class is created to represent a date/time object of a task, which is stored inside a `Task` object
upon creating a `Task` through an `add` command. Inside `TaskDate.java` class, date and time are represented by Java 
class objects `LocalDate` and `LocalTime`. As the user can choose to either omit both date and time or have time as optional
information, both of these objects are wrapped in Java `Optional` objects as `Optional<LocalDate>` and `Optional<LocalTime>`.
This makes handling of a `TaskDate` object safer as the date and time objects do not exist in a `Task` which the user adds
in without stating date/time information. 

The user is also required to follow a specific date and time format which the validity is checked by `DateTask#isValidTaskDate`.
Java `LocalDate#parse` and `LocalTime#parse` are used to help verify validity of the format keyed in by the user.

Example usage of `add` command to add date/time is as follows:
- `add d/Homework dt/21/11/2021` - Adds a task "Homework" that is due on 21/11/2021 at an unspecified time.
- `add d/Tutorial dt/1600` - Adds a task "Tutorial" that starts at 4:00 PM with date as that current day.
- `add d/Event dt/25/10/2021, 10:00 AM` - Adds a task "Event" that starts at the given date and time.

A sequence diagram is provided below that shows how TaskDate class works when the command "add d/Homework dt/21/11/2021"
is entered:
![AddDateSequenceDiagram](images/AddDateSequenceDiagram.png)

The following activity diagram summarises what happens when a user executes an add command with date and/or time:
![AddDateActivityDiagram](images/AddDateActivityDiagram.png)

#### Design considerations:

**Aspect: How to store date and time in TaskDate:**

* **Alternative 1 (current choice):** Uses `LocalDate` and `LocalTime` to store date and time respectively.
    * Pros: More flexibility and better abstraction as both can be handled separately. Less complex to implement.
    * Cons: More methods and code are needed to handle the different types of Object separately which results in 
            methods with similar code.

* **Alternative 2:** Uses `LocalDateTime` to store date and time together.
    * Pros: Less code and methods to handle one Object type and it also results in easier implementation of
            comparison methods between two `LocalDateTime` objects.
    * Cons: Less flexibility and more tedious to check different combinations of DateTime formats.

### \[Implemented\] Listing Upcoming Tasks (Hanif)

With the implementation of `TaskDate.java`, we can easily look up upcoming tasks by comparing dates and times.

#### Implementation
In `UpcomingTaskCommand.java`, a field of type `Predicate<Task>` named `TaskDateAfterCurrentDatePredicate` is used to encapsulate the logic of determining whether or not a task is upcoming. It contains fields that store the current date and time via `LocalDateTime.now()`, and the current date via `LocalDate.now()`.
The overridden `Predicate#test(Task task)` method first checks if the `task.TaskDate` object has a date, then if it has a date but no time, and finally whether it has both date and time.

When `UpcomingTaskCommand.execute(Model model)` is called by `LogicManager`, `UpcomingTaskCommand` passes `TaskDateAfterCurrentDatePredicate` to `Model#updateFilteredTaskList(Predicate<Task> predicate)`, which filters out the upcoming tasks and updates the viewable `ObservableList<Task>`. 

A sequence diagram is provided below:
![UpcomingTaskCommandSequenceDiagram](images/UpcomingTaskCommandSequenceDiagram.png)

#### Proposed Extension
Currently, `UpcomingTaskCommand` returns upcoming tasks in the same order that they are listed in the original task list. They are not sorted by date. A proposed extension would be to implement a new method that iterates through the filtered list and sorts the tasks by date after `Model#updateFilteredTaskList` is called since it simply filters the list with the predicate.

#### Alternatives Considered
* Changing the syntax of the `upcoming` command to include a number i.e. `upcoming 7` to indicate upcoming tasks in the next 7 days.
  * Pros: More specific, limits the scope of viewable tasks which would help usability.
  * Cons: Not immediately clear what unit of time the number would represent. 
    On one hand, it would be too limiting if we as developers decided the unit of time, and on the other hand it would be too clunky to have the user define it themselves. 
    Implementing the proposed extension of sorting upcoming tasks would better address the issue. Additionally, having unnecessarily longer syntax would decrease the speed at which the user would be able to operate, which goes against Dash's primary design goal of prioritizing speed.
    

### \[Implemented\] Using the Up/Down Arrow Keys to Select Previous User Inputs (Joshua)

An implemented improvement to the text-based input method is to allow users to easily reenter previously inputted 
commands by retrieving their past inputs to the CLI using the up and down arrow keys. We feel that this is a subtle 
feature which greatly improves the speed and usability of the app.

An example use case would be when a user wants to add two tasks with descriptions "CS2100 Tutorial 7" and "CS2100 
Tutorial 8" to their task List. Instead of typing out a near-identical command for the second task, they could press 
the up arrow key, access their previously entered commmand and change '7' to '8'.

Another example use case would be when a user accidentally deletes an entry in Dash by entering the wrong index. As 
long as the entry was added within the past 10 commands, the user can press the up arrow key until the command that 
corresponds to adding that entry is set in the command box. The user can then simply press enter to add the entry again.

#### Implementation

The implementation involves adding a new class `UserInputList` to `Model`. When a user enters an input in the command 
box, the `UserInputList` is updated in `LogicManager` if this user input results in a valid command execution. On app 
startup and after any successful command execution, the `CommandBox` is reinitialised with an updated list of user 
input strings from the `Model`.

In order to allow the user to scroll through their past inputs to select a particular one, we must keep track of which 
input string the CLI is currently displaying. This is implemented in `CommandBox` by keeping track of the index of the 
string being displayed within the list of input strings. When the up or down arrow keys are pressed, `CommandBox` 
increments or decrements the index, retrieves the new string corresponding to it, and displays it. 

The storage of the `UserInputList` works similarly to the storage of the `TaskList` and `AddressBook`. When the app is 
started, a `UserInputList` is constructed using the `userinputlist.json` file in the `data` folder. Conversely, when 
the app is closed, the `userinputlist.json` file is updated using the `UserInputList`.

#### Design considerations

- Keeping track of different lists of user inputs for different tabs

We decided against keeping track of different lists of user inputs corresponding to commands on different tabs. We 
judged the benefits of this feature to be minimal and not worth the extra complexity in `Model` and `Storage`. 
Moreover, we would have to make an arbitrary decision on where to store the user inputs corresponding to switching 
tabs.
  
- Storing the index of the list in `Model`

Due to the fact that the index should reset between uses of the app, we decided that there is not need to store the 
index of the currently in-focus user input in `Model` or `Storage`. The index can safely be reset to 0 upon input of a 
command or upon opening the app. 
  
- Storing user inputs that are invalid

We decided not to store user inputs that are invalid due to the current behaviour of the text box: when an invalid 
input is entered, the input remains in the text box with a red font. In the case of a typo, since the user can easily 
modify this previous input, there seems to be no need to store it.

- Storing more user inputs

The current implementation only stores 10 past user inputs. We decided to put a cap on the number of inputs stored due 
to concerns about performance when the user input list is updated. This cap is set in `UserInputList` as 
`LIST_MAX_LENGTH`. If, in testing, we find that a higher cap could be more convenient for users, this value can easily 
be changed.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* A student,
  * Students generally have different types of tasks they need to organize and deadlines to keep track of.
  * Students have many contacts from various places such as modules, CCAs, etc, that they would like to organize better.

* On his laptop/computer very often,
  * Typing is mainly an advantage with a physical keyboard
  * Some students may choose to avoid using their mobile phone often to avoid distractions

* A person who prefers/is more skilled at typing,
  * Typing is only faster with experience
  * Tasks and deadlines are easier to be specified with typing, instead of clicking through multiple options with a laptop trackpad
  * Students may encounter situations where they need to type information/deadlines very quickly (e.g. during a lecture)

**Value proposition**:
 * Manage contacts and tasks faster than a typical mouse/GUI driven app.

### User stories

| Priority | As a(n) ...                                | I want to ...                                 | So that I can ...                                               |
|----------|--------------------------------------------|-----------------------------------------------|-----------------------------------------------------------------|
| * * *    | user                                       | create tasks                                  | record the tasks I need to do                                   |
| * * *    | user                                       | delete tasks                                  | clear my list of tasks if it gets too cluttered                 |
| * * *    | user                                       | view all tasks                                | see all the tasks I have at the moment                          |
| * * *    | user                                       | edit tasks                                    | keep track of changing requirements                             |
| * * *    | user                                       | add contacts                                  | store them for later reference                                  |
| * * *    | user                                       | delete contacts                               | remove contacts when I want to                                  |
| * * *    | user                                       | view all contacts                             | keep track of all my contacts                                   |
| * * *    | user                                       | edit contacts                                 | change them later when my contacts update their contact details |
| * * *    | first-time user                            | view a list of available commands             | refer to them easily                                            |
| * * *    | user who prefers typing                    | use a CLI over a GUI                          | be faster and efficient in using the app                        |
| * *      | experienced user                           | write notes on the main page of the app       | see miscellaneous reminders at a glance                         |
| * *      | forgetful student                          | add short notes to each task as a reminder    | Remember what the task is about                                 |
| * *      | stressed student with many tasks to handle | set priorities of tasks                       | decide what to do next                                          |
| * *      | lazy student                               | go back to previously used full command lines | be even faster without needing to type                          |
| * *      | student with many contacts                 | group contacts according to tags              | organise my contacts better                                     |
| * *      | student with many CCAs                     | tag tasks with CCA tags                       | easily identify what I need to do for a particular CCA          |
| * *      | student taking many modules                | tag tasks with module tags                    | easily identify what I need to do for a particular module       |

### Use cases

#### Use case 01: Add a task

<u>MSS:</u>

1. User switches to the task tab
2. Dash shows the list of tasks
3. User requests to add a task, specifying task info
4. Dash adds task to the list

Use case ends.

<u>Extensions:</u>

3. There is no task description specified
    * a. Dash shows an error message

Use case resumes at step 2.

#### Use case 02: Delete a task

<u>MSS:</u>

1. User switches to the task tab
2. Dash shows the list of tasks
3. User requests to delete a task, specifying the index to be deleted
4. Dash deletes the task

Use case ends.

<u>Extension:</u>

3. The list is empty

Use case ends.

3. The given index is invalid
    * a. Dash shows an error message

Use case resumes at step 2.

#### Use case 03: Edit a task

<u>MSS:</u>

1. User switches to the task tab
2. Dash shows the list of tasks
3. User inputs the task index to be edited, specifying updated info for the task
4. Dash edits the task

Use case ends.

<u>Extension:</u>

3.  The task list is empty

Use case ends

3. The given index is invalid
   * a. Dash shows an error message

Use case resumes at step 3

#### Use case 04: View all tasks

<u>MSS:</u>

1. User switches to the task tab
2. Dash shows the list of tasks

Use case ends.

<u>Extension:</u>

2. The task list is empty

Use case ends.

#### Use case 05: Add a contact

<u>MSS:</u>

1. User switches to the contacts tab
2. Dash shows a list of contacts
3. User requests to add a contact, specifying contact info
4. Dash adds contact to the list

Use case ends.

<u>Extension:</u>

3. The contact info is invalid
    * a. Dash shows an error message

Use case resumes at step 2.

#### Use case 06: Delete a contact

<u>MSS:</u>

1. User switches to the contacts tab
2. Dash shows the list of contacts
3. User requests to delete a contact, specifying the index to be deleted
4. Dash deletes the contact

Use case ends.

<u>Extension:</u>

3. The contacts list is empty

Use case ends.

3. The given index is invalid
    * a. Dash shows an error message

Use case resumes at step 2.

#### Use case 07: Edit a contact

<u>MSS:</u>

1. User switches to contacts tab
2. Dash shows the list of contacts
3. User inputs the contact index to be edited, specifying the updated info for the contact
4. Dash edits the contact

Use case ends.

<u>Extension:</u>

3. The contact list is empty

Use case ends.

3. The given index is invalid
   * Dash shows an error message

Use case resumes at step 2.

#### Use case 08: View all contact

<u>MSS:</u>

1. User switches to the contacts tab
2. Dash shows the list of contacts

Use case ends.

<u>Extension:</u>

2a. The task list is empty

Use case ends.

#### Use case 09: View a list of available commands

<u>MSS:</u>

1. User requests to view all available commands
2. Dash shows a list of all available commands

Use case ends.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be lightweight.
5. Should run smoothly even on low-end systems.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases … }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases … }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases … }_
