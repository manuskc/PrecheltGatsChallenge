package com.manuskc.precheltgatschallenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MobileToString {

	private Encoder encoder;
	private String mobilesFileName;

	public MobileToString(Encoder theEncoder, String theMobilesFileName) {
		this.encoder = theEncoder;
		this.mobilesFileName = theMobilesFileName;
	}

	public void printMobileToWordMapping() {
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(this.mobilesFileName));
			String mobile;
			while((mobile = fileReader.readLine()) != null) {
				List<String> results = new ArrayList<String>();
				findWords(0, new ArrayList<Integer>(), "", stripNonNumbers(mobile), false, "", results); //$NON-NLS-1$ //$NON-NLS-2$
				if(!results.isEmpty()) {
					for(String result: results) {
						System.out.println(mobile+": "+result); //$NON-NLS-1$
					}
				}
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String stripNonNumbers(String mobile) {
		return mobile.replaceAll("[^0-9]", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void findWords(int index, List<Integer> lastNumberList, String accumulatedToMatch, String mobile, boolean isPreviousNumber, String currentResult,  List<String> result) {
		if(index >= mobile.length()) {
			//if we have nothing left to match on reaching end of mobile - we have got a result
			if(accumulatedToMatch.isEmpty()) result.add(currentResult);
			return;
		}

		//check if we have a word from last number index
		if(!lastNumberList.isEmpty()) {
			for(Integer numberIndex : lastNumberList) {
				if(this.encoder.getCodeMap().get(mobile.substring(numberIndex.intValue(), index+1)) != null) {
					return; //no need to process further
				}
			}
		}

		//if accumulated to match is present in encoder - expand current result and continue;
		List<String> codes = this.encoder.getCodeMap().get(accumulatedToMatch+mobile.charAt(index));
		if(codes != null) {
			for(String code: codes) {
				findWords(index+1, lastNumberList, "", mobile, false, currentResult+(currentResult.isEmpty() ? "" : " ")+code, result);   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
			}
		}

		//if previous character is not a number, try with using it as a number;
		if(!isPreviousNumber && accumulatedToMatch.isEmpty()) {
			lastNumberList.add(new Integer(index));
			findWords(index+1, lastNumberList, accumulatedToMatch, mobile, true, currentResult+(currentResult.isEmpty() ? "" : " ")+mobile.charAt(index), result); //$NON-NLS-1$ //$NON-NLS-2$
			lastNumberList.remove(new Integer(index));
		}

		//accumulate a character and continue processing;

		findWords(index+1, lastNumberList, accumulatedToMatch+mobile.charAt(index), mobile, false, currentResult, result);
	}

	public static void main(String[] args) {
		Encoder encoder = new Encoder("dictionary.txt"); //$NON-NLS-1$
		MobileToString mobileToString = new MobileToString(encoder, "mobiles.txt"); //$NON-NLS-1$
		mobileToString.printMobileToWordMapping();
	}
}
