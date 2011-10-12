package graphage;

import javax.swing.filechooser.FileFilter;

import java.io.File;

import java.util.Vector;

public class ImageFileFilter extends FileFilter {
	
	private Vector<String> extensions;
	private String description;
	
	public ImageFileFilter(){
		extensions = new Vector<String>();
	}
	
	public void addExtension(String _filter){
		extensions.add(_filter.toLowerCase());
	}

	@Override
	public boolean accept(File file) {
		for (String ext : extensions){
			if (file.getName().toLowerCase().endsWith(ext)){
				return true;
		    }else if(file.isDirectory()){
		    	return true;
		    }
		}
		return false;
	}
	
	public void setDescription(String desc){
		this.description = desc;
	}
	
	@Override
	public String getDescription(){
		return description;
	}
}
