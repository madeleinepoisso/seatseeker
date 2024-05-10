package edu.brown.cs.student.main.server.BackEndTesting;

import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.main.server.Event;
import edu.brown.cs.student.main.server.EventSorter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class Testing {
  List<Event> emptyEventList = MockedData.createEmptyEventList();
  List<Event> EventList = MockedData.createEventListWithVariousEvents();
  List<Event> sameEventList = MockedData.createEventListWithSameEvent();
  EventSorter sorter = new EventSorter();

  @Test
  public void emptyEventsList(){
    List<Event> result = this.sorter.bucketSort(this.emptyEventList,null,null,null);
    assertEquals(result,new ArrayList<>());
  }

  @Test
  public void timeParameter(){
    List<Event> result = this.sorter.bucketSort(this.EventList,null,"19:30",null);
    assertEquals(result.get(0).time,"19:30");
  }

  @Test
  public void locationParameter(){
    List<Event> result = this.sorter.bucketSort(this.EventList,null,null,"Boston");
    assertEquals(result.get(0).city,"Boston");
  }

  @Test
  public void dateParameter(){
    List<Event> result = this.sorter.bucketSort(this.EventList,"Dec 27",null,null);
    assertEquals(result.get(0).date,"Dec 27");
  }

  @Test
  public void eventsWithinRadius(){
    List<Event> result = this.sorter.bucketSort(this.EventList,null,null,"Brockton");
    assertEquals(result.get(0).name,"Ballet");
  }

  @Test
  public void repeatEvent(){
    List<Event> result = this.sorter.bucketSort(this.sameEventList,null,null,null);
    assertEquals(result.get(0),);
  }
}

