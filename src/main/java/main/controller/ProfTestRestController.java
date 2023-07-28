package main.controller;

import main.logic.JwtConfigs.JwtTokenUtil;
import main.logic.entities.AnswerOption;
import main.logic.entities.Role;
import main.logic.entities.TestPoint;
import main.logic.entities.User;
import main.logic.services.JwtUserDetailsService;
import main.logic.services.ProfTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class ProfTestRestController {

    @Autowired
    JwtUserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ProfTestService testService;

    @GetMapping("")
    public List<TestPoint> getAllTestPoints(){
        return testService.getAllTests();
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> addTestPoint(@RequestBody TestPoint testPoint, @RequestHeader("Authorization") String token, @PathVariable int courseId) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {
            if(testService.addTestPoint(testPoint,courseId).isPresent()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/options/{testId}")
    public ResponseEntity<?> updateTestPoint(@RequestBody List<AnswerOption> answerOptions, @RequestHeader("Authorization") String token, @PathVariable int testId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {
            if(testService.updateTestPoint(answerOptions,testId).isPresent()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{testId}")
    public ResponseEntity<?> deleteTestPoint(@RequestHeader("Authorization") String token,@PathVariable int testId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {
           if(testService.deleteTestPoint(testId).isPresent()){
               return new ResponseEntity<>(HttpStatus.OK);
           }
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteAllTestByCourseId(@RequestHeader("Authorization") String token,@PathVariable int courseId){
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.getUser(email);

        if(user.getRole() == Role.teacher) {
            if(testService.deleteAllTestByCourseId(courseId).isEmpty()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}


