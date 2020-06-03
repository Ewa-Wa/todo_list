package com.ewawaszczuk.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ToDoData {
    private static ToDoData instance = new ToDoData();
    private static String fileName = "ToDoListItems.txt";
    private ObservableList<ToDoItem> toDoItems;
    private DateTimeFormatter formatter;


    public static ToDoData getInstance() {
        return instance;
    }

    private ToDoData(){
        formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    }

    public ObservableList<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void addToDoItem(ToDoItem item){
        toDoItems.add(item);
    }

    public void loadToItems() throws IOException{  // READS TODOITEMS FROM THE FILE
        toDoItems = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader reader = Files.newBufferedReader(path);

        String input;
        try{
            while ((input = reader.readLine()) != null){
            String[] itemParts = input.split("\t");

            String shortDescription = itemParts[0];
            String details = itemParts[1];
            String dateString = itemParts[2];

            LocalDate date = LocalDate.parse(dateString,formatter);
            ToDoItem toDoItem = new ToDoItem(shortDescription, details, date);
            toDoItems.add(toDoItem);
            }
        } finally {
            if(reader !=null){
                reader.close();
            }
        }
    }

    public void storeToDoItems() throws IOException { // WRITE DATA TO TODOLIST
        Path path = Paths.get(fileName);
        BufferedWriter writer = Files.newBufferedWriter(path);
        try{
            Iterator<ToDoItem> iter = toDoItems.iterator();
            while(iter.hasNext()){
                ToDoItem item = iter.next();
                writer.write(String.format("%s\t%s\t%s", // jeśli dużo danych to lepiej tu nie formować
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadline().format(formatter)));
                writer.newLine();
            }
        }finally {
            if(writer != null) {
                writer.close();
            }
        }
    }
    public void deleteToDoItem(ToDoItem item){
        toDoItems.remove(item);
    }
}
