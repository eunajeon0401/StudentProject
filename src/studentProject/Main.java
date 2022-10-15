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
				System.out.println("���α׷��� ���� �˴ϴ�. Have a nice day~~");
				loopFlag = true;
				break;
			default:
				System.out.println("���� 1 ~ 8������ �ٽ� �Է¹ٶ��ϴ�.");
				break;

			}

		}
	System.out.println("���α׷�����");
	}
	
	public static void studentStats() {
		List<Student> list = new ArrayList<Student>();
		try {
			System.out.print("1. �ְ� ����,  2. ���� ���� >>");
			int type = sc.nextInt();
			
			boolean value = checkInputPattern(String.valueOf(type), 5);
			if(!value) return;
			
			//����Ÿ���̽� ���� 
		    DBConnection dbConn = new DBConnection();
		    dbConn.connect();
		    
		    list = dbConn.selectMaxMin(type);
		    
		    if(list.size() <= 0) {
		    	System.out.println("�л� ������ �����ϴ�" + list.size());
		    	return;
		    }
		    //����Ʈ ������ �����ش�
		    for(Student student : list) {
		    	System.out.println(student);
		    }
		    dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ� ���Է¿�û"+e.getMessage());
			return;
		}catch(Exception e){
			System.out.println("����Ÿ���̽� �л���� ����" + e.getMessage());
		}
	}

	public static void studentSort() {
		List<Student> list = new ArrayList<Student>();
		try {
			//����Ÿ���̽� ����, �л����� ����
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			//���Ĺ�� ����
			System.out.print("���Ĺ��(1 : no, 2 : name, 3 : total)>>");
			int type = sc.nextInt();
			//���ϰ˻�
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if(!value) return;
			
			//���̺� ������ �Է�
			list = dbConn.selectOrderBy(type);
			
			if(list.size() <= 0) {
				System.out.println("������ ����Ʈ�� �����ϴ�"+list.size());
				return;
			}
			//����Ʈ ������ �����ش�.
			for(Student student : list) {
				System.out.println(student);
			}
		}catch(Exception e){
				System.out.println("����Ÿ���̽� ���� ����" + e.getMessage());
		}
		return;
		

	}
	public static void studentUpDate() {
		List<Student> list = new ArrayList<Student>();
		//������ �л���ȣ �Է�
		System.out.print("������ �л� ��ȣ �Է�>>");
		String no = sc.nextLine();
		
		boolean value = checkInputPattern(no, 1);
		if(!value) return;
		
		//��ȣ�� �˻��ؼ� �ҷ����� �Ѵ�, ����Ÿ���̽� ���� �л���ȣ �˻�
		DBConnection dbConn = new DBConnection();
	    dbConn.connect();
	    //���̺� ������ �Է�
	    list = dbConn.selectSearch(no, 1);
	    if(list.size() <= 0) {
	    	System.out.println("������ �л� ������ �����ϴ�"+list.size());
	    }
	    //����Ʈ�� �����ش�
	    for(Student student : list) {
	    	System.out.println(student);
	    }
	    
	    //������ ����Ʈ�� �����ش�
	    //��,��,�� ���� ���Է�
	    Student imsiStudent = list.get(0);
	    System.out.print("����"+imsiStudent.getKor()+">>");
	    int kor = sc.nextInt();
	    value = checkInputPattern(String.valueOf(kor), 3);
	    if(!value) return;
	    imsiStudent.setKor(kor);
	    
	    System.out.print("����"+imsiStudent.getEng()+">>");
	    int eng = sc.nextInt();
	    value = checkInputPattern(String.valueOf(eng), 3);
	    if(!value) return;
	    imsiStudent.setEng(eng);
	    
	    System.out.print("����"+imsiStudent.getMath()+">>");
	    int math = sc.nextInt();
	    value = checkInputPattern(String.valueOf(math), 3);
	    if(!value) return;
	    imsiStudent.setMath(math);
	    
	    //����,���,���
//	    imsiStudent.calTotal();
//	    imsiStudent.calAvr();
//	    imsiStudent.calGrade();
	    
	    //����Ÿ���̽��� ������ �κ��� update�� �ؾ��Ѵ�
	    int returnUpdateValue = dbConn.update(imsiStudent);
	    if(returnUpdateValue == -1) {
	    	System.out.println("�л� ������ �����ȵƽ��ϴ�"+returnUpdateValue);
	    	return;
	    }
	    System.out.println("�л� ������ ���� �Ǿ����ϴ�"+returnUpdateValue);
	    
		
		
	}
	public static void studentOutput() {
		List<Student> list = new ArrayList<Student>();
		try { 
			//����Ÿ���̽� ����, �л����� ��ü���
		     DBConnection dbConn = new DBConnection();
		     dbConn.connect();
		     
		     //���̺� ����Ÿ �Է�
		     list = dbConn.select();
		     if(list.size() <= 0) {
		    	 System.out.println("������ ����Ʈ�� �����ϴ� "+list.size());
		     }
		     //����Ʈ ������ �����ش�
		     for(Student student : list) {
		    	 System.out.println(student);
		     }
		     dbConn.close();
		}catch(Exception e){
			System.out.println("����Ÿ���̽� ��� ����" + e.getMessage());
		}
		return;
	}
	public static void studentSearch() {
		List<Student> list = new ArrayList<Student>();
		try {
			//�˻��� �л��̸� �Է�
			System.out.print("�˻��� �л� �̸��� �Է��Ͻÿ�");
			String name = sc.nextLine();
			//���ϰ˻�
			boolean value = checkInputPattern(name, 2);
			if(!value) {
				return;
			}
			//����Ÿ���̽� ���� �л��̸��˻�
			DBConnection dbConn = new DBConnection();
		    dbConn.connect();
		    //student ���̺� ������ �Է�
		    list = dbConn.selectSearch(name,2);
		    if(list.size() <= 0) {
		    	System.out.println("�˻��� �л������� �����ϴ� "+ list.size());
		    	return;
		    }
		    //list ������ �����ش�
		    for(Student student : list) {
		    	System.out.println(student);
		    }
		    dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ� ���Է¿�û"+e.getStackTrace());
			return;
		}catch(Exception e){
			System.out.println("����Ÿ���̽� �л��˻� ����" + e.getStackTrace());
		}
}
	//�л����� ����
	public static void studentDelete() {
		try {
			//������ �һ���ȣ �Է�
			System.out.print("������ �л���ȣ �Է� (010101)>>");
			String no = sc.nextLine();
			//���ڿ� ���ϰ˻�
			boolean value = checkInputPattern(no,1);
			if(!value) return;
			
			//����Ÿ���̽� �Է� ,����
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			//���̺� ������ �Է�
			int deleteReturnValue = dbConn.delete(no);
			if(deleteReturnValue == -1) {
				System.out.println("���� �����Դϴ�" + deleteReturnValue);
			}if(deleteReturnValue == 0) {
				System.out.println("������ ��ȣ�� �������� �ʽ��ϴ�");
			}else {
				System.out.println("���� �����Դϴ� ���ϰ� =" + deleteReturnValue);
			}
			dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ� ���Է¿�û"+e.getStackTrace());
			return;
		}catch(Exception e){
			System.out.println("����Ÿ���̽� ���� ����" + e.getStackTrace());
		}finally {
			//sc.nextLine();
		}

	}
	
	//�л����� �Է�
	public static void studentInputData() {
		String pattern = null;
		boolean regex = false;
		

		try {
			System.out.print("�г�(01,02,03)��(01~09)��ȣ(01~60)>>");
			String no = sc.nextLine();
			//no ���ڿ�
			boolean value = checkInputPattern(no, 1);
			if(!value) return;
			
			System.out.print("�̸��Է¿��");
			String name = sc.nextLine();
			//name ���ڿ� ���ϰ˻�
			value = checkInputPattern(name, 2);
			if(!value) return;
			
			System.out.print("kor�Է¿��");
			int kor = sc.nextInt();
			//kor (0~100) ���ϰ˻�
			value = checkInputPattern(String.valueOf(kor), 3);
			if(!value) return;

			System.out.print("eng�Է¿��");
			int eng = sc.nextInt();
			//eng (0~100) ���ϰ˻�
			value = checkInputPattern(String.valueOf(eng), 3);
			if(!value) return;
		
			System.out.print("math�Է¿��");
			int math = sc.nextInt();
			//math (0~100)���ϰ˻�
			value = checkInputPattern(String.valueOf(kor), 3);
			
			//�л� ��ü�����ؼ� ���� ���� �ִ´� 
			Student student = new Student(no,name,kor,eng,math);
//			student.calTotal();
//			student.calAvr();
//			student.calGrade();
			
			//����Ÿ���̽� �Է�
			DBConnection dbConn = new DBConnection();
			//����Ÿ���̽� ����
			dbConn.connect();
			
			//���̺� �������Է�
			int insertReturnValue = dbConn.insert(student);
			if(insertReturnValue == -1) {
				System.out.println("���Խ���");
			}else {
				System.out.println("���Լ��� ���ϰ� = " + insertReturnValue);
			}
			dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("�Է��� Ÿ���� ���� �ʽ��ϴ� ���Է¿�û"+e.getMessage());
			return;
		}catch(Exception e){
			System.out.println("����Ÿ���̽� �Է� ����" + e.getMessage());
		}finally {
			sc.nextLine();
		}

	}

	//�޴� �Է�
	public static int displayMenu() {
		int num = -1;
		//�޴� �Է°�
		try {
			System.out.print("------------------------------------------------------------------"
					+ "\n1.�Է� || 2.���� || 3.���� || 4.�˻� || 5.��� || 6.���� || 7.��� || 8.���� "
					+ "\n------------------------------------------------------------------\n�Է¹ٶ��ϴ� >>>>>>>");
			num = sc.nextInt();
			String pattern = "^[1-8]$"; 
			boolean regex = Pattern.matches(pattern, String.valueOf(num));
		}catch (InputMismatchException e) {
			System.out.println("���ڸ� �Է� ��Ź�帳�ϴ�");
		}finally {
			//�Է���ġ���������
			sc.nextLine();
		}
		return num;
	}

	//���� �˻�
	public static boolean checkInputPattern(String data, int patternType) {
		String pattern= null;
		boolean regex = false;
		String message = null;
		switch(patternType) {
		case 1 :
			pattern = "^0[1-3]0[1-9][0-6][0-9]$";
			message = "no ���Է¿��";
			break;
		case 2 :
			pattern = "^[��-�R]{2,4}$"; 
			message = "�̸� ���Է¿��";
			break;
		case 3 :
			pattern = "^[0-9]{1,3}$"; 
			message = "���� ���Է¿��";
			break;
		case 4 :
			pattern = "^[1-3]$"; 
			message = "����Ÿ�� ���Է¿��";
			break;
		case 5 :
			pattern = "^[1-2]$";
			message = "���Ÿ�� ���Է¿��";
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

