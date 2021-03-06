package audio;

import java.io.File;


public class Assignment6 {
	public static void main(String[] args) {		
		if(!ParameterChecker.CheckFormat(args)){
			System.exit(-1);
		};
		String[] paths1=getFilePaths(args[1],args[0]);
		String[] paths2=getFilePaths(args[3],args[2]);
		Comparator comparator = new Comparator(paths1,paths2);
		comparator.compare();
	}
	

	
	public static String[] getFilePaths(String path, String parameter){
		String[] filePaths=null;
		if(parameter.trim().equals("-f")){
			filePaths=new String[1];
			filePaths[0]=path;
		}
		else if(parameter.trim().equals("-d")){
			filePaths=new File(path).list();
			String[] tmp=new String[filePaths.length];
			for(int i=0;i<tmp.length;i++){
				tmp[i]=path+'/'+filePaths[i];
			}
			filePaths=tmp;
		}
		return filePaths;
	}

}

