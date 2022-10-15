package studentProject;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	public static final int INPUT = 1,UPDATE =2, DELETE =3, SEARCH =4, OUTPUT =5;
	public static final int SORT =6, STATS = 7, EXIT =8;
	public static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		boolean loopFlag = false;
		
		while(! loopFlag) {
			int menuNo = displayMenu();
			switch(menuNo) {
			case INPUT:
				studentInputData();
				break;
			case UPDATE:
				studentUpDate();
				break;
			case DELETE:
				studentDelete();
				break;
			case SEARCH:
				studentSearch();
				break;
			case OUTPUT:
				studentOutput();
				break;
			case SORT:
				studentSort();
				break;
			case STATS:
				studentStats();
				break;
			case EXIT:
				System.out.println("ÇÁ·Î±×·¥ÀÌ Á¾·á µË´Ï´Ù. Have a nice day~~");
				loopFlag = true;
				break;
			default:
				System.out.println("¼ıÀÚ 1 ~ 8¹ø±îÁö ´Ù½Ã ÀÔ·Â¹Ù¶ø´Ï´Ù.");
				break;

			}

		}
	System.out.println("ÇÁ·Î±×·¥Á¾·á");
	}
	
	public static void studentStats() {
		List<Student> list = new ArrayList<Student>();
		try {
			System.out.print("1. ÃÖ°í Á¡¼ö,  2. ÃÖÀú Á¡¼ö >>");
			int type = sc.nextInt();
			
			boolean value = checkInputPattern(String.valueOf(type), 5);
			if(!value) return;
			
			//µ¥ÀÌÅ¸º£ÀÌ½º ¿¬°á 
		    DBConnection dbConn = new DBConnection();
		    dbConn.connect();
		    
		    list = dbConn.selectMaxMin(type);
		    
		    if(list.size() <= 0) {
		    	System.out.println("ÇĞ»ı Á¤º¸°¡ ¾ø½À´Ï´Ù" + list.size());
		    	return;
		    }
		    //¸®½ºÆ® ³»¿ëÀ» º¸¿©ÁØ´Ù
		    for(Student student : list) {
		    	System.out.println(student);
		    }
		    dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù ÀçÀÔ·Â¿äÃ»"+e.getMessage());
			return;
		}catch(Exception e){
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º ÇĞ»ıÅë°è ¿¡·¯" + e.getMessage());
		}
	}

	public static void studentSort() {
		List<Student> list = new ArrayList<Student>();
		try {
			//µ¥ÀÌÅ¸º£ÀÌ½º ¿¬°á, ÇĞ»ıÁ¤º¸ Á¤·Ä
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			//Á¤·Ä¹æ½Ä ¼±ÅÃ
			System.out.print("Á¤·Ä¹æ½Ä(1 : no, 2 : name, 3 : total)>>");
			int type = sc.nextInt();
			//ÆĞÅÏ°Ë»ö
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if(!value) return;
			
			//Å×ÀÌºí µ¥ÀÌÅÍ ÀÔ·Â
			list = dbConn.selectOrderBy(type);
			
			if(list.size() <= 0) {
				System.out.println("º¸¿©ÁÙ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù"+list.size());
				return;
			}
			//¸®½ºÆ® ³»¿ëÀ» º¸¿©ÁØ´Ù.
			for(Student student : list) {
				System.out.println(student);
			}
		}catch(Exception e){
				System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º Á¤·Ä ¿¡·¯" + e.getMessage());
		}
		return;
		

	}
	public static void studentUpDate() {
		List<Student> list = new ArrayList<Student>();
		//¼öÁ¤ÇÒ ÇĞ»ı¹øÈ£ ÀÔ·Â
		System.out.print("¼öÁ¤ÇÒ ÇĞ»ı ¹øÈ£ ÀÔ·Â>>");
		String no = sc.nextLine();
		
		boolean value = checkInputPattern(no, 1);
		if(!value) return;
		
		//¹øÈ£·Î °Ë»öÇØ¼­ ºÒ·¯³»¾ß ÇÑ´Ù, µ¥ÀÌÅ¸º£ÀÌ½º ¿¬°á ÇĞ»ı¹øÈ£ °Ë»ö
		DBConnection dbConn = new DBConnection();
	    dbConn.connect();
	    //Å×ÀÌºí µ¥ÀÌÅÍ ÀÔ·Â
	    list = dbConn.selectSearch(no, 1);
	    if(list.size() <= 0) {
	    	System.out.println("¼öÁ¤ÇÒ ÇĞ»ı Á¤º¸°¡ ¾ø½À´Ï´Ù"+list.size());
	    }
	    //¸®½ºÆ®¸¦ º¸¿©ÁØ´Ù
	    for(Student student : list) {
	    	System.out.println(student);
	    }
	    
	    //¼öÁ¤ÇÒ ¸®½ºÆ®¸¦ º¸¿©ÁØ´Ù
	    //±¹,¿µ,¼ö Á¡¼ö ÀçÀÔ·Â
	    Student imsiStudent = list.get(0);
	    System.out.print("±¹¾î"+imsiStudent.getKor()+">>");
	    int kor = sc.nextInt();
	    value = checkInputPattern(String.valueOf(kor), 3);
	    if(!value) return;
	    imsiStudent.setKor(kor);
	    
	    System.out.print("¿µ¾î"+imsiStudent.getEng()+">>");
	    int eng = sc.nextInt();
	    value = checkInputPattern(String.valueOf(eng), 3);
	    if(!value) return;
	    imsiStudent.setEng(eng);
	    
	    System.out.print("¼öÇĞ"+imsiStudent.getMath()+">>");
	    int math = sc.nextInt();
	    value = checkInputPattern(String.valueOf(math), 3);
	    if(!value) return;
	    imsiStudent.setMath(math);
	    
	    //ÃÑÇÕ,Æò±Õ,µî±Ş
//	    imsiStudent.calTotal();
//	    imsiStudent.calAvr();
//	    imsiStudent.calGrade();
	    
	    //µ¥ÀÌÅ¸º£ÀÌ½º¿¡ ¼öÁ¤ÇÒ ºÎºĞÀ» update¸¦ ÇØ¾ßÇÑ´Ù
	    int returnUpdateValue = dbConn.update(imsiStudent);
	    if(returnUpdateValue == -1) {
	    	System.out.println("ÇĞ»ı Á¤º¸°¡ ¼öÁ¤¾ÈµÆ½À´Ï´Ù"+returnUpdateValue);
	    	return;
	    }
	    System.out.println("ÇĞ»ı Á¤º¸°¡ ¼öÁ¤ µÇ¾ú½À´Ï´Ù"+returnUpdateValue);
	    
		
		
	}
	public static void studentOutput() {
		List<Student> list = new ArrayList<Student>();
		try { 
			//µ¥ÀÌÅ¸º£ÀÌ½º ¿¬°á, ÇĞ»ıÁ¤º¸ ÀüÃ¼Ãâ·Â
		     DBConnection dbConn = new DBConnection();
		     dbConn.connect();
		     
		     //Å×ÀÌºí µ¥ÀÌÅ¸ ÀÔ·Â
		     list = dbConn.select();
		     if(list.size() <= 0) {
		    	 System.out.println("º¸¿©ÁÙ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù "+list.size());
		     }
		     //¸®½ºÆ® ³»¿ëÀ» º¸¿©ÁØ´Ù
		     for(Student student : list) {
		    	 System.out.println(student);
		     }
		     dbConn.close();
		}catch(Exception e){
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º Ãâ·Â ¿¡·¯" + e.getMessage());
		}
		return;
	}
	public static void studentSearch() {
		List<Student> list = new ArrayList<Student>();
		try {
			//°Ë»öÇÒ ÇĞ»ıÀÌ¸§ ÀÔ·Â
			System.out.print("°Ë»öÇÒ ÇĞ»ı ÀÌ¸§À» ÀÔ·ÂÇÏ½Ã¿À");
			String name = sc.nextLine();
			//ÆĞÅÏ°Ë»ö
			boolean value = checkInputPattern(name, 2);
			if(!value) {
				return;
			}
			//µ¥ÀÌÅ¸º£ÀÌ½º ¿¬°á ÇĞ»ıÀÌ¸§°Ë»ö
			DBConnection dbConn = new DBConnection();
		    dbConn.connect();
		    //student Å×ÀÌºí µ¥ÀÌÅÍ ÀÔ·Â
		    list = dbConn.selectSearch(name,2);
		    if(list.size() <= 0) {
		    	System.out.println("°Ë»öÇÑ ÇĞ»ıÁ¤º¸°¡ ¾ø½À´Ï´Ù "+ list.size());
		    	return;
		    }
		    //list ³»¿ëÀ» º¸¿©ÁØ´Ù
		    for(Student student : list) {
		    	System.out.println(student);
		    }
		    dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù ÀçÀÔ·Â¿äÃ»"+e.getStackTrace());
			return;
		}catch(Exception e){
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º ÇĞ»ı°Ë»ö ¿¡·¯" + e.getStackTrace());
		}
}
	//ÇĞ»ıÁ¤º¸ »èÁ¦
	public static void studentDelete() {
		try {
			//»èÁ¦ÇÒ ÇÒ»ı¹øÈ£ ÀÔ·Â
			System.out.print("»èÁ¦ÇÒ ÇĞ»ı¹øÈ£ ÀÔ·Â (010101)>>");
			String no = sc.nextLine();
			//¹®ÀÚ¿­ ÆĞÅÏ°Ë»ö
			boolean value = checkInputPattern(no,1);
			if(!value) return;
			
			//µ¥ÀÌÅ¸º£ÀÌ½º ÀÔ·Â ,¿¬°á
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			//Å×ÀÌºí µ¥ÀÌÅÍ ÀÔ·Â
			int deleteReturnValue = dbConn.delete(no);
			if(deleteReturnValue == -1) {
				System.out.println("»èÁ¦ ½ÇÆĞÀÔ´Ï´Ù" + deleteReturnValue);
			}if(deleteReturnValue == 0) {
				System.out.println("»èÁ¦ÇÒ ¹øÈ£°¡ Á¸ÀçÇÏÁö ¾Ê½À´Ï´Ù");
			}else {
				System.out.println("»èÁ¦ ¼º°øÀÔ´Ï´Ù ¸®ÅÏ°ª =" + deleteReturnValue);
			}
			dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù ÀçÀÔ·Â¿äÃ»"+e.getStackTrace());
			return;
		}catch(Exception e){
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º »èÁ¦ ¿¡·¯" + e.getStackTrace());
		}finally {
			//sc.nextLine();
		}

	}
	
	//ÇĞ»ıÁ¤º¸ ÀÔ·Â
	public static void studentInputData() {
		String pattern = null;
		boolean regex = false;
		

		try {
			System.out.print("ÇĞ³â(01,02,03)¹İ(01~09)¹øÈ£(01~60)>>");
			String no = sc.nextLine();
			//no ¹®ÀÚ¿­
			boolean value = checkInputPattern(no, 1);
			if(!value) return;
			
			System.out.print("ÀÌ¸§ÀÔ·Â¿ä¸Á");
			String name = sc.nextLine();
			//name ¹®ÀÚ¿­ ÆĞÅÏ°Ë»ö
			value = checkInputPattern(name, 2);
			if(!value) return;
			
			System.out.print("korÀÔ·Â¿ä¸Á");
			int kor = sc.nextInt();
			//kor (0~100) ÆĞÅÏ°Ë»ö
			value = checkInputPattern(String.valueOf(kor), 3);
			if(!value) return;

			System.out.print("engÀÔ·Â¿ä¸Á");
			int eng = sc.nextInt();
			//eng (0~100) ÆĞÅÏ°Ë»ö
			value = checkInputPattern(String.valueOf(eng), 3);
			if(!value) return;
		
			System.out.print("mathÀÔ·Â¿ä¸Á");
			int math = sc.nextInt();
			//math (0~100)ÆĞÅÏ°Ë»ö
			value = checkInputPattern(String.valueOf(kor), 3);
			
			//ÇĞ»ı °´Ã¼»ı¼ºÇØ¼­ À§¿¡ °ªÀ» ³Ö´Â´Ù 
			Student student = new Student(no,name,kor,eng,math);
//			student.calTotal();
//			student.calAvr();
//			student.calGrade();
			
			//µ¥ÀÌÅ¸º£ÀÌ½º ÀÔ·Â
			DBConnection dbConn = new DBConnection();
			//µ¥ÀÌÅ¸º£ÀÌ½º ¿¬°á
			dbConn.connect();
			
			//Å×ÀÌºí µ¥ÀÌÅÍÀÔ·Â
			int insertReturnValue = dbConn.insert(student);
			if(insertReturnValue == -1) {
				System.out.println("»ğÀÔ½ÇÆĞ");
			}else {
				System.out.println("»ğÀÔ¼º°ø ¸®ÅÏ°ª = " + insertReturnValue);
			}
			dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("ÀÔ·ÂÇÑ Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù ÀçÀÔ·Â¿äÃ»"+e.getMessage());
			return;
		}catch(Exception e){
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º ÀÔ·Â ¿¡·¯" + e.getMessage());
		}finally {
			sc.nextLine();
		}

	}

	//¸Ş´º ÀÔ·Â
	public static int displayMenu() {
		int num = -1;
		//¸Ş´º ÀÔ·Â°ª
		try {
			System.out.print("------------------------------------------------------------------"
					+ "\n1.ÀÔ·Â || 2.¼öÁ¤ || 3.»èÁ¦ || 4.°Ë»ö || 5.Ãâ·Â || 6.Á¤·Ä || 7.Åë°è || 8.Á¾·á "
					+ "\n------------------------------------------------------------------\nÀÔ·Â¹Ù¶ø´Ï´Ù >>>>>>>");
			num = sc.nextInt();
			String pattern = "^[1-8]$"; 
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
		}catch (InputMismatchException e) {
			System.out.println("¼ıÀÚ¸¸ ÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù");
		}finally {
			//ÀÔ·ÂÀåÄ¡¹öÆÛÁö¿ì±â
			sc.nextLine();
		}
		return num;
	}

	//ÆĞÅÏ °Ë»ö
	public static boolean checkInputPattern(String data, int patternType) {
		String pattern= null;
		boolean regex = false;
		String message = null;
		switch(patternType) {
		case 1 :
			pattern = "^0[1-3]0[1-9][0-6][0-9]$";
			message = "no ÀçÀÔ·Â¿ä¸Á";
			break;
		case 2 :
			pattern = "^[°¡-ÆR]{2,4}$"; 
			message = "ÀÌ¸§ ÀçÀÔ·Â¿ä¸Á";
			break;
		case 3 :
			pattern = "^[0-9]{1,3}$"; 
			message = "Á¡¼ö ÀçÀÔ·Â¿ä¸Á";
			break;
		case 4 :
			pattern = "^[1-3]$"; 
			message = "Á¤·ÄÅ¸ÀÔ ÀçÀÔ·Â¿ä¸Á";
			break;
		case 5 :
			pattern = "^[1-2]$";
			message = "Åë°èÅ¸ÀÔ ÀçÀÔ·Â¿ä¸Á";
			break;
		}
		regex = Pattern.matches(pattern, data);
		if(patternType == 3) {
			if(!regex || Integer.parseInt(data) < 0 || Integer.parseInt(data) > 100) {
				System.out.println(message);
				return false;
			}
		}else {
			if(!regex) {
				System.out.println(message);
				return false;
			}
		}
		return regex;
	}

}

