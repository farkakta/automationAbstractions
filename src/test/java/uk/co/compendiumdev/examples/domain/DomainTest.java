package uk.co.compendiumdev.examples.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import uk.co.compendiumdev.examples.domain.actions.TodoActions;
import uk.co.compendiumdev.examples.domain.actors.TodoMVCUser;
import uk.co.compendiumdev.examples.domain.pojofordomain.TodoMVCDomainPojoPage;
import uk.co.compendiumdev.examples.domain.todos.RandomTodoGenerator;
import uk.co.compendiumdev.examples.domain.todos.ToDoItem;
import uk.co.compendiumdev.examples.domain.todos.ToDoList;
import uk.co.compendiumdev.examples.pojo.TodoMVCPojoPage;
import uk.co.compendiumdev.selenium.support.webdriver.ExecutionDriver;
import uk.co.compendiumdev.todomvc.site.TodoMVCSite;

public class DomainTest {

    /*
        All previous tests and abstractions have been Domain abstractions.

        But domain has been more physical - web, browser, page etc.

        Now we move into a more 'logical' set of abstractions.

        - users
        - actions that someone might make on a page e.g. create multiple todos
        - To do List objects - the list, the items

     */

    private WebDriver driver;
    private TodoMVCSite todoMVCSite;

    private TodoMVCDomainPojoPage todoMVC;

    @Before
    public void setup(){
        driver = new ExecutionDriver().get();
        todoMVCSite = new TodoMVCSite();

        todoMVC = new TodoMVCDomainPojoPage(driver, todoMVCSite.getURL());
        todoMVC.open();
    }


    /*
        Domain objects are often used to represent 'things' in the domain.

        e.g. a To Do Item or a list of to do items

        This can be useful for modelling data stored in a file, or from API messages
        or for randomly generating data. Then we can use that in our tests more easily.
     */


    @Test
    public void todoItemDomainObjects(){

        ToDoList myTodos = new ToDoList();

        myTodos.addNewToDoItem("My First Todo");
        myTodos.addNewToDoItem("My Second Todo");
        myTodos.addNewToDoItem("My Third Todo");

        for(ToDoItem todo : myTodos.todos()){
            todoMVC.enterNewToDo(todo.getText());
        }

        Assert.assertEquals(myTodos.size(), todoMVC.getTodoItems().size());

        for(int index=0; index < todoMVC.getTodoItems().size(); index++){
            Assert.assertEquals(myTodos.getItemAtPosition(index).getText(),
                                todoMVC.getToDoText(index));
        }
    }

    @Test
    public void todoItemRandomDomainObjects(){

        ToDoList myTodos = new ToDoList();

        RandomTodoGenerator rnd = new RandomTodoGenerator();

        myTodos.addNewToDoItem(rnd.getRandomTodoName());
        myTodos.addNewToDoItem(rnd.getRandomTodoName());
        myTodos.addNewToDoItem(rnd.getRandomTodoName());

        for(ToDoItem todo : myTodos.todos()){
            todoMVC.enterNewToDo(todo.getText());
        }

        Assert.assertEquals(myTodos.size(), todoMVC.getTodoItems().size());

        for(int index=0; index < todoMVC.getTodoItems().size(); index++){
            Assert.assertEquals(myTodos.getItemAtPosition(index).getText(),
                    todoMVC.getToDoText(index));
        }
    }
    /*
        We might want to create high level 'actors' which model users or other
        systems in the domain.

        e.g. a User, an AdminUser etc.

     */
    @Test
    public void aUserDomainObject(){

        TodoMVCUser user = new TodoMVCUser(driver, todoMVCSite);

        user.
            createNewToDo("My First Todo").
            and().
            createNewToDo("My Second Todo");

        Assert.assertEquals(2, todoMVC.getTodoItems().size());

        user.deleteToDoAt(1);

        Assert.assertEquals(1, todoMVC.getTodoItems().size());
    }

    /* TODO
            EXERCISE - expand the User object so that they can
                       editTodoAt(position) and
                       markTodoAsCompleted(position) and
                       markTodoAsNotDone(position) and
                       countNumberOfTodos()
                       and use these methods in tests
    */

    @Test
    public void createMultipleTodosWithAnActionsObject(){

        /*
            It is easy to use a fluent interface to create multiple to do items

            page.open().and().
                createTodo("My First To do").
                createTodo("My Second To do").
                createTodo("My Third To do").
                createTodo("My Fourth To do").

            But that might lead to very long tests.

            Perhaps it is appropriate to create action abstractions that
            'do stuff' in a readable and maintainable way?

         */

        new TodoActions(driver).
                createMultipleTodos("My First To do", "My Second To do",
                                    "My Third To do", "My Fourth To do");


        Assert.assertEquals(4, todoMVC.getTodoItems().size());

        new TodoActions(driver).
                createTodosOffset(10, "Todo %d Item ", 5);

        Assert.assertEquals(14, todoMVC.getTodoItems().size());

        /*
            TODO:
                    QUESTION:
                                What other actions might be appropriate that don't
                                'belong' on another domain object? e.g. getLastTodo
                                deleteLastXTodos? deleteLastTodo? renameAllTodos?
                    EXERCISE:
                                Implement some 'action' level methods and use them in
                                a test.
         */

    }

/*
    TODO:
        QUESTION - should a User have a performsAction() method
                    which returns a TodoActions object?
                    That might make tests easier to write
                    with fewer lines of code.
                    What are the pros and cons?
        EXERCISE -
                    Try it and see?
 */

/*
EXERCISE - combine the TodoList and the User so that the User uses and maintains
a todolist in its actions e.g. whena user creates a to do it adds it to the page
and keeps track of it in a todolist object. This user can then be used to model
the expectations, as well as taking actions e.g. user.expectedNumberOfTodos(),
user.countNumberOfTodos()
 */



    @After
    public void teardown(){
        ExecutionDriver.closeDriver(driver);
    }
}
