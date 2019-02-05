package com.github.kmizu.jcombinator;

import com.github.kmizu.jcombinator.datatype.Function1;
import com.github.kmizu.jcombinator.datatype.Procedure1;


public interface ParseResult<T> {
	public static class Success<T> implements ParseResult<T> {
		private T value;
		private String next;
		Success(T value, String next) {
			this.value = value;
			this.next = next;
		}
		public T value() {
			return value;
		}
		public String next() {
			return next;
		}
		
		@Override
		public <U> U fold(
		  Function1<Success<T>, U> succ,
		  Function1<Failure<T>, U> fail) {
			return succ.invoke(this);
		}
	
		@Override
		public void fold(
			Procedure1<Success<T>> succ,
			Procedure1<Failure<T>> fail
		) {
			succ.invoke(this);
		}

		@Override
		public <U> ParseResult<U> map(Function1<T, U> fn) {
			return new Success<U>(fn.invoke(value), next);
		}

		@Override
		public void onSuccess(Procedure1<Success<T>> succ) {
			succ.invoke(this);
		}
	}
	
	public static class Failure<T> implements ParseResult<T> {		
		private String message;
		private String next;
		public Failure(String message, String next) {
			this.message = message;
			this.next = next;
		}
		public String message() {
			return message;
		}
		public String next() {
			return next;
		}
		
		@Override
		public <U> U fold(
		  Function1<Success<T>, U> succ,
		  Function1<Failure<T>, U> fail) {
			return fail.invoke(this);
		}
		
		@Override
		public void fold(
			Procedure1<Success<T>> succ,
			Procedure1<Failure<T>> fail
		) {
			fail.invoke(this);
		}

		@Override
		public <U> ParseResult<U> map(Function1<T, U> fn) {
			return (ParseResult<U>)this;
		}
	    
		@Override
		public void onSuccess(Procedure1<Success<T>> succ) {
			
		}
	}
	
	<U> U fold(
		Function1<Success<T>, U> succ,
	    Function1<Failure<T>, U> fail
	);
	
	void fold(
		Procedure1<Success<T>> succ,
		Procedure1<Failure<T>> fail
	);
	
	void onSuccess(Procedure1<Success<T>> succ);

	<U> ParseResult<U> map(Function1<T, U> fn);
}
