package com.dkhalife.projects;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * This class implements a Simple File Filter
 * 
 * @author Dany Khalife
 * @version 1.0
 * @since December, 2012
 * 
 */
public class SimpleFileFilter extends FileFilter {
	// The description of the file filter
	private String m_description = null;

	// The extension of the files to filter
	private String m_extension = null;

	/**
	 * 
	 * To construct a Simple File Filter all you need is an extension and a
	 * description for this Filter
	 * 
	 * @param extension The extension of the files you want to filter
	 * @param description The description of the file filter
	 * 
	 */
	public SimpleFileFilter(String extension, String description) {
		m_description = description;
		m_extension = "." + extension.toLowerCase();
	}

	/**
	 * 
	 * Getter for the description
	 * 
	 * @return The description of the filter
	 * 
	 */
	public String getDescription() {
		return m_description;
	}

	/**
	 * 
	 * The accept implementation accepts a file only if its extension is the
	 * same as specified in this Filter
	 * 
	 */
	public boolean accept(File f) {
		if (f == null)
			return false;
		if (f.isDirectory())
			return true;
		return f.getName().toLowerCase().endsWith(m_extension);
	}
}
