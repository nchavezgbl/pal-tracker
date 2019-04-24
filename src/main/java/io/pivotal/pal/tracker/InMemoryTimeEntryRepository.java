package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

  private HashMap<Long, TimeEntry> timeEntries = new HashMap<Long, TimeEntry>();
  private long currentTimeEntryId = 1L;

  public TimeEntry create(TimeEntry timeEntry) {

    // update the entry to use the same value as the hashmap id value
    TimeEntry newTimeEntry = new TimeEntry(currentTimeEntryId, timeEntry.getProjectId(),
        timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
    timeEntries.put(currentTimeEntryId, newTimeEntry);
    currentTimeEntryId++;

    return newTimeEntry;
  }

  public TimeEntry find(long timeEntryId) {
    //returns null if not found
    return timeEntries.get(timeEntryId);

  }

  public List<TimeEntry> list() {
    return timeEntries.values().stream().collect(Collectors.toList());
  }

  public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {

    if(timeEntries.containsKey(timeEntryId)){
      TimeEntry updatedTimeEntry = new TimeEntry(timeEntryId, timeEntry.getProjectId(),
          timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
      timeEntries.put(timeEntryId, updatedTimeEntry);
      return updatedTimeEntry;
    }else{
      return null;
    }


  }

  public void delete(long timeEntryId) {

    if(this.find(timeEntryId) != null){
      timeEntries.remove(timeEntryId);
      // do not decrement timeEntryId
    }
  }
}
