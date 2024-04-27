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
public class WordTranslateResponse {
    String trans;
    List<DictEntry> dict;
    
    @Override
    public String toString() {
    	StringBuilder res = new StringBuilder("\t"+trans+"\n");
    	if (dict != null) {
			for (int i = 0; i < dict.size(); i++)
				res.append(dict.get(i).toString() + "\n");
		}
		return res.toString();
    }
}
