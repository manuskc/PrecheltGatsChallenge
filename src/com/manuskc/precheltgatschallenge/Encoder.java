package com.manuskc.precheltgatschallenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Encoder {
	
	/*
	 * E | J N Q | R W X | D S Y | F T | A M | C I V | B K U | L O P | G H Z
	 * e | j n q | r w x | d s y | f t | a m | c i v | b k u | l o p | g h z
	 * 0 |   1   |   2   |   3   |  4  |  5  |   6   |   7   |   8   |   9
	 */
	private static int keyMappingData[] = {5,7,6,3,0,4,9,9,6,1,7,8,5,1,8,8,1,2,3,4,7,6,2,2,3,9};
	
	private String filename;
	private Map<String, List<String>> codeMap;
	
	public Encoder(String theFilename) {
		this.codeMap = new HashMap<String, List<String>>();
		this.filename = theFilename;
		loadDictionary();
	}
	
	private void loadDictionary() {
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(this.filename));
			String word;
			while((word = fileReader.readLine()) != null) {
				String encodedMobileNumber = encode(word);
				List<String> codeList = this.getCodeMap().get(encodedMobileNumber);
				if(codeList == null) {
					codeList = new ArrayList<String>();
					codeList.add(word);
					this.getCodeMap().put(encodedMobileNumber, codeList);
				} else {
					codeList.add(word);
				}
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private static String encode(String word) {
		String result = ""; //$NON-NLS-1$
		for(char c: word.toLowerCase().toCharArray()) {
			if(c >= 'a' && c <= 'z') {
				result += keyMappingData[c - 'a'];
			}
		}
		return result;
	}

	public Map<String, List<String>> getCodeMap() {
		return this.codeMap;
	}
}
