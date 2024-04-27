package com.engSpace.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictEntry {

	private String pos;
	private List<String> terms;
	private List<EntryTranslate> entry;
	private String base_form;
	private int pos_enum;
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("pos: "+ pos+ "\n");
		if (entry != null) {
			for (int i = 0; i < entry.size(); i++)
				res.append(entry.get(i).toString() + "\n");
		}
		res.append("base form: "+base_form+ "\n");
		return  res.toString();
	}
}
