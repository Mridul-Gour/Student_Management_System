package com.cts.sms.exception;


public class CourseActionException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

	public CourseActionException(String message) {
        super(message);
    }
}
