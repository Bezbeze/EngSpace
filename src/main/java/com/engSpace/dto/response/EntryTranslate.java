package com.engSpace.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntryTranslate {

	String word;
	List<String> reverse_translation;
	double score;
	
	@Override
	public String toString(){
		return word + " reverse translation: " + reverse_translation.toString();
	}
	
}
