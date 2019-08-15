package uk.co.compendiumdev.todomvc.page.functionalvsstructural;

import uk.co.compendiumdev.todomvc.page.structural.loadablecomponent.ApplicationPageStructuralLoadable;
import uk.co.compendiumdev.todomvc.page.structural.pagefactory.ApplicationPageStructuralFactory;
import uk.co.compendiumdev.todomvc.page.structural.pojo.ApplicationPageStructural;
import uk.co.compendiumdev.todomvc.page.structural.pojo.StructuralApplicationPage;
import uk.co.compendiumdev.todomvc.page.structural.slowloadablecomponent.ApplicationPageStructuralSlowLoadable;
import uk.co.compendiumdev.todomvc.site.TodoMVCSite;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import static uk.co.compendiumdev.todomvc.page.structural.pojo.StructuralEnums.Filter.*;
import static uk.co.compendiumdev.todomvc.page.structural.pojo.StructuralEnums.ItemsInState.*;

public class ApplicationPageFunctional {

    // switch between different structural implementations
    private StructuralApplicationPage structure;
    //private ApplicationPageStructural structure;
    //private ApplicationPageStructuralFactory structure;
    //private ApplicationPageStructuralLoadable structure;
    //private ApplicationPageStructuralSlowLoadable structure;

    public static ApplicationPageFunctional getPojoBacked(WebDriver driver, TodoMVCSite todoMVCSite){
        return new ApplicationPageFunctional(
                new ApplicationPageStructural(driver, todoMVCSite),
                driver, todoMVCSite);
    }

    public static ApplicationPageFunctional getFactoryBacked(WebDriver driver, TodoMVCSite todoMVCSite){
        return new ApplicationPageFunctional(
                new ApplicationPageStructuralFactory(driver, todoMVCSite),
                driver, todoMVCSite);
    }

    public static ApplicationPageFunctional getLoadableBacked(WebDriver driver, TodoMVCSite todoMVCSite){
        return new ApplicationPageFunctional(
                new ApplicationPageStructuralLoadable(driver, todoMVCSite),
                driver, todoMVCSite);
    }

    public static ApplicationPageFunctional getSlowLoadableBacked(WebDriver driver, TodoMVCSite todoMVCSite){
        return new ApplicationPageFunctional(
                new ApplicationPageStructuralSlowLoadable(driver, todoMVCSite),
                driver, todoMVCSite);
    }

    public ApplicationPageFunctional(WebDriver driver, TodoMVCSite todoMVCSite) {

        // by default use the Pojo
        structure = new ApplicationPageStructural(driver, todoMVCSite);
        //structure = new ApplicationPageStructuralFactory(driver, todoMVCSite);
        //structure = new ApplicationPageStructuralLoadable(driver, todoMVCSite);
        //structure = new ApplicationPageStructuralSlowLoadable(driver, todoMVCSite);
    }

    public ApplicationPageFunctional(StructuralApplicationPage structuralPage, WebDriver driver, TodoMVCSite todoMVCSite) {
        this.structure = structuralPage;
    }

    public int getCountOfTodoDoItems() {
        return structure.getCountOfTodo(VISIBLE);
    }

    public int getCountOfCompletedTodoDoItems() {
        return structure.getCountOfTodo(VISIBLE_COMPLETED);
    }

    public int getCountOfActiveTodoDoItems() {
        return structure.getCountOfTodo(VISIBLE_ACTIVE);
    }

    public String getLastToDoIext() {
        return structure.getToDoText(getCountOfTodoDoItems()-1);
    }

    public void enterNewToDo(String todoTitle) {
        // could be Keys.ENTER on 2.40.0 and below
        // but needs to be Keys.RETURN on 2.41.0
        structure.typeIntoNewToDo(todoTitle, Keys.RETURN);
    }

    public void open() {
        structure.open();
    }

    public boolean isFooterVisible() {
        // catch any not found exceptions at functional layer
        try{
            return structure.isFooterVisible();
        }catch(NoSuchElementException e){
            return false;
        }
    }

    public boolean isMainVisible() {
        try{
            return structure.isMainVisible();
        }catch(NoSuchElementException e){
            return false;
        }
    }

    public void deleteLastToDo() {
        structure.deleteTodoItem(getCountOfTodoDoItems()-1);
    }


    public void editLastItem(String editTheTitleTo) {
        structure.editItem(getCountOfTodoDoItems()-1,editTheTitleTo);
    }


    public Integer getCountInFooter() {
        try{
            return structure.getCountInFooter();
        }catch(NoSuchElementException e){
            return 0;
        }
    }

    public String getCountTextInFooter() {

        String countText = "";
        try{
            countText = structure.getCountTextInFooter();
        }catch(NoSuchElementException e){

        }

        return countText;
    }

    public void filterOnAll() {
        structure.clickOnFilter(ALL);
    }

    public void filterOnActive() {
        structure.clickOnFilter(ACTIVE);
    }

    public void filterOnCompleted() {
        structure.clickOnFilter(COMPLETED);
    }

    public void toggleCompletionOfLastItem() {
        structure.toggleCompletionOfItem(getCountOfTodoDoItems() - 1);
    }

    public boolean isClearCompletedVisible() {
        return structure.isClearCompletedVisible();
    }

    public Integer getClearCompletedCount() {

        return structure.getClearCompletedCount();
    }

    public void clearCompleted() {
        structure.clickClearCompleted();
    }
}
