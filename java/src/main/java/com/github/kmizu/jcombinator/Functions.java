package com.github.kmizu.jcombinator;

import com.github.kmizu.jcombinator.datatype.Function1;
import com.github.kmizu.jcombinator.datatype.Procedure1;
import com.github.kmizu.jcombinator.datatype.Tuple2;

import java.util.List;

public class Functions {

    public static <T, U> U let(T value, Function1<T, U> fn) {
        return fn.invoke(value);
    }

    public static <T> void let(T value, Procedure1<T> pr) {
        pr.invoke(value);
    }
    
    public static <T, U> U foldLeft(List<T> list, U init, Function1<Tuple2<U, T>, U> f) {
    	U result = init;
    	for(T t:list) {
    		result = f.invoke(new Tuple2<>(result, t));
    	}
    	return result;
    }
    
    public static <T> String join(List<T> list, String separator) {
    	if(list.size() == 0) {
    		return "";
    	} else {
    		return let(new StringBuilder(), builder -> {
    		    T t = list.get(0);
    		    List<T> es = list.subList(1, list.size());
    			builder.append(t.toString());
    			for(T e:es) {
    				builder.append(separator);
    				builder.append(e.toString());
    			}
    			return new String(builder);
    		});
    	}
    }
}
