package com.redoct.iclub.util;

import java.util.Comparator;

import com.redoct.iclub.item.ContactItem;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<ContactItem> {

	public int compare(ContactItem o1, ContactItem o2) {
		
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

	

}
