package com.engSpace.entity;

import java.time.LocalDate;

import com.engSpace.dto.response.WordTranslateResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordEntity {
	String word;
	WordTranslateResponse wordData;
    LocalDate nextReview;
    Integer reviewCount;

}
