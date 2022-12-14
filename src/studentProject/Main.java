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
				System.out.println("프로그램이 종료 됩니다. Have a nice day~~");
				loopFlag = true;
				break;
			default:
				System.out.println("숫자 1 ~ 8번까지 다시 입력바랍니다.");
				break;

			}

		}
	System.out.println("프로그램종료");
	}
	
	public static void studentStats() {
		List<Student> list = new ArrayList<Student>();
		try {
			System.out.print("1. 최고 점수,  2. 최저 점수 >>");
			int type = sc.nextInt();
			
			boolean value = checkInputPattern(String.valueOf(type), 5);
			if(!value) return;
			
			//데이타베이스 연결 
		    DBConnection dbConn = new DBConnection();
		    dbConn.connect();
		    
		    list = dbConn.selectMaxMin(type);
		    
		    if(list.size() <= 0) {
		    	System.out.println("학생 정보가 없습니다" + list.size());
		    	return;
		    }
		    //리스트 내용을 보여준다
		    for(Student student : list) {
		    	System.out.println(student);
		    }
		    dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다 재입력요청"+e.getMessage());
			return;
		}catch(Exception e){
			System.out.println("데이타베이스 학생통계 에러" + e.getMessage());
		}
	}

	public static void studentSort() {
		List<Student> list = new ArrayList<Student>();
		try {
			//데이타베이스 연결, 학생정보 정렬
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			//정렬방식 선택
			System.out.print("정렬방식(1 : no, 2 : name, 3 : total)>>");
			int type = sc.nextInt();
			//패턴검색
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if(!value) return;
			
			//테이블 데이터 입력
			list = dbConn.selectOrderBy(type);
			
			if(list.size() <= 0) {
				System.out.println("보여줄 리스트가 없습니다"+list.size());
				return;
			}
			//리스트 내용을 보여준다.
			for(Student student : list) {
				System.out.println(student);
			}
		}catch(Exception e){
				System.out.println("데이타베이스 정렬 에러" + e.getMessage());
		}
		return;
		

	}
	public static void studentUpDate() {
		List<Student> list = new ArrayList<Student>();
		//수정할 학생번호 입력
		System.out.print("수정할 학생 번호 입력>>");
		String no = sc.nextLine();
		
		boolean value = checkInputPattern(no, 1);
		if(!value) return;
		
		//번호로 검색해서 불러내야 한다, 데이타베이스 연결 학생번호 검색
		DBConnection dbConn = new DBConnection();
	    dbConn.connect();
	    //테이블 데이터 입력
	    list = dbConn.selectSearch(no, 1);
	    if(list.size() <= 0) {
	    	System.out.println("수정할 학생 정보가 없습니다"+list.size());
	    }
	    //리스트를 보여준다
	    for(Student student : list) {
	    	System.out.println(student);
	    }
	    
	    //수정할 리스트를 보여준다
	    //국,영,수 점수 재입력
	    Student imsiStudent = list.get(0);
	    System.out.print("국어"+imsiStudent.getKor()+">>");
	    int kor = sc.nextInt();
	    value = checkInputPattern(String.valueOf(kor), 3);
	    if(!value) return;
	    imsiStudent.setKor(kor);
	    
	    System.out.print("영어"+imsiStudent.getEng()+">>");
	    int eng = sc.nextInt();
	    value = checkInputPattern(String.valueOf(eng), 3);
	    if(!value) return;
	    imsiStudent.setEng(eng);
	    
	    System.out.print("수학"+imsiStudent.getMath()+">>");
	    int math = sc.nextInt();
	    value = checkInputPattern(String.valueOf(math), 3);
	    if(!value) return;
	    imsiStudent.setMath(math);
	    
	    //총합,평균,등급
//	    imsiStudent.calTotal();
//	    imsiStudent.calAvr();
//	    imsiStudent.calGrade();
	    
	    //데이타베이스에 수정할 부분을 update를 해야한다
	    int returnUpdateValue = dbConn.update(imsiStudent);
	    if(returnUpdateValue == -1) {
	    	System.out.println("학생 정보가 수정안됐습니다"+returnUpdateValue);
	    	return;
	    }
	    System.out.println("학생 정보가 수정 되었습니다"+returnUpdateValue);
	    
		
		
	}
	public static void studentOutput() {
		List<Student> list = new ArrayList<Student>();
		try { 
			//데이타베이스 연결, 학생정보 전체출력
		     DBConnection dbConn = new DBConnection();
		     dbConn.connect();
		     
		     //테이블 데이타 입력
		     list = dbConn.select();
		     if(list.size() <= 0) {
		    	 System.out.println("보여줄 리스트가 없습니다 "+list.size());
		     }
		     //리스트 내용을 보여준다
		     for(Student student : list) {
		    	 System.out.println(student);
		     }
		     dbConn.close();
		}catch(Exception e){
			System.out.println("데이타베이스 출력 에러" + e.getMessage());
		}
		return;
	}
	public static void studentSearch() {
		List<Student> list = new ArrayList<Student>();
		try {
			//검색할 학생이름 입력
			System.out.print("검색할 학생 이름을 입력하시오");
			String name = sc.nextLine();
			//패턴검색
			boolean value = checkInputPattern(name, 2);
			if(!value) {
				return;
			}
			//데이타베이스 연결 학생이름검색
			DBConnection dbConn = new DBConnection();
		    dbConn.connect();
		    //student 테이블 데이터 입력
		    list = dbConn.selectSearch(name,2);
		    if(list.size() <= 0) {
		    	System.out.println("검색한 학생정보가 없습니다 "+ list.size());
		    	return;
		    }
		    //list 내용을 보여준다
		    for(Student student : list) {
		    	System.out.println(student);
		    }
		    dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다 재입력요청"+e.getStackTrace());
			return;
		}catch(Exception e){
			System.out.println("데이타베이스 학생검색 에러" + e.getStackTrace());
		}
}
	//학생정보 삭제
	public static void studentDelete() {
		try {
			//삭제할 할생번호 입력
			System.out.print("삭제할 학생번호 입력 (010101)>>");
			String no = sc.nextLine();
			//문자열 패턴검색
			boolean value = checkInputPattern(no,1);
			if(!value) return;
			
			//데이타베이스 입력 ,연결
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			//테이블 데이터 입력
			int deleteReturnValue = dbConn.delete(no);
			if(deleteReturnValue == -1) {
				System.out.println("삭제 실패입니다" + deleteReturnValue);
			}if(deleteReturnValue == 0) {
				System.out.println("삭제할 번호가 존재하지 않습니다");
			}else {
				System.out.println("삭제 성공입니다 리턴값 =" + deleteReturnValue);
			}
			dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다 재입력요청"+e.getStackTrace());
			return;
		}catch(Exception e){
			System.out.println("데이타베이스 삭제 에러" + e.getStackTrace());
		}finally {
			//sc.nextLine();
		}

	}
	
	//학생정보 입력
	public static void studentInputData() {
		String pattern = null;
		boolean regex = false;
		

		try {
			System.out.print("학년(01,02,03)반(01~09)번호(01~60)>>");
			String no = sc.nextLine();
			//no 문자열
			boolean value = checkInputPattern(no, 1);
			if(!value) return;
			
			System.out.print("이름입력요망");
			String name = sc.nextLine();
			//name 문자열 패턴검색
			value = checkInputPattern(name, 2);
			if(!value) return;
			
			System.out.print("kor입력요망");
			int kor = sc.nextInt();
			//kor (0~100) 패턴검색
			value = checkInputPattern(String.valueOf(kor), 3);
			if(!value) return;

			System.out.print("eng입력요망");
			int eng = sc.nextInt();
			//eng (0~100) 패턴검색
			value = checkInputPattern(String.valueOf(eng), 3);
			if(!value) return;
		
			System.out.print("math입력요망");
			int math = sc.nextInt();
			//math (0~100)패턴검색
			value = checkInputPattern(String.valueOf(kor), 3);
			
			//학생 객체생성해서 위에 값을 넣는다 
			Student student = new Student(no,name,kor,eng,math);
//			student.calTotal();
//			student.calAvr();
//			student.calGrade();
			
			//데이타베이스 입력
			DBConnection dbConn = new DBConnection();
			//데이타베이스 연결
			dbConn.connect();
			
			//테이블 데이터입력
			int insertReturnValue = dbConn.insert(student);
			if(insertReturnValue == -1) {
				System.out.println("삽입실패");
			}else {
				System.out.println("삽입성공 리턴값 = " + insertReturnValue);
			}
			dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("입력한 타입이 맞지 않습니다 재입력요청"+e.getMessage());
			return;
		}catch(Exception e){
			System.out.println("데이타베이스 입력 에러" + e.getMessage());
		}finally {
			sc.nextLine();
		}

	}

	//메뉴 입력
	public static int displayMenu() {
		int num = -1;
		//메뉴 입력값
		try {
			System.out.print("------------------------------------------------------------------"
					+ "\n1.입력 || 2.수정 || 3.삭제 || 4.검색 || 5.출력 || 6.정렬 || 7.통계 || 8.종료 "
					+ "\n------------------------------------------------------------------\n입력바랍니다 >>>>>>>");
			num = sc.nextInt();
			String pattern = "^[1-8]$"; 
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
		}catch (InputMismatchException e) {
			System.out.println("숫자만 입력 부탁드립니다");
		}finally {
			//입력장치버퍼지우기
			sc.nextLine();
		}
		return num;
	}

	//패턴 검색
	public static boolean checkInputPattern(String data, int patternType) {
		String pattern= null;
		boolean regex = false;
		String message = null;
		switch(patternType) {
		case 1 :
			pattern = "^0[1-3]0[1-9][0-6][0-9]$";
			message = "no 재입력요망";
			break;
		case 2 :
			pattern = "^[가-힣]{2,4}$"; 
			message = "이름 재입력요망";
			break;
		case 3 :
			pattern = "^[0-9]{1,3}$"; 
			message = "점수 재입력요망";
			break;
		case 4 :
			pattern = "^[1-3]$"; 
			message = "정렬타입 재입력요망";
			break;
		case 5 :
			pattern = "^[1-2]$";
			message = "통계타입 재입력요망";
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

