package io.pivotal.pal.tracker;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
  private final TimeEntryRepository timeEntryRepository;

  public TimeEntryController(TimeEntryRepository timeEntryRepository) {
    this.timeEntryRepository = timeEntryRepository;
  }
  @PostMapping
  //return a ReponseEntity with a newly created TimeEntry and the HTTPS status
  public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
    TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);

    return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.CREATED);
  }
  @GetMapping("/{timeEntryId}")
  public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
    TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
    if(timeEntry != null){
      return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }else{
      return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public ResponseEntity<List<TimeEntry>> list() {
    List<TimeEntry> timeEntryList = timeEntryRepository.list();
    return new ResponseEntity<List<TimeEntry>>(timeEntryList, HttpStatus.OK);
  }

  @PutMapping("/{timeEntryId}")
  public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
    TimeEntry updatedTimeEntry = timeEntryRepository.update(timeEntryId, expected);
    if(updatedTimeEntry != null){
      return new ResponseEntity<TimeEntry>(updatedTimeEntry, HttpStatus.OK);
    }else{
      return new ResponseEntity<TimeEntry>(updatedTimeEntry, HttpStatus.NOT_FOUND);
    }
  }
  @DeleteMapping("/{timeEntryId}")
  public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
    timeEntryRepository.delete(timeEntryId);
    return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
  }
}
