package studentProject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

	public class DBConnection {
		private Connection connection = null;
	    private Statement statement = null;
	    private ResultSet rs = null;

 	    // connection
	    public void connect() {
	    	Properties properties = new Properties();

	    	try {
	    		FileInputStream fis = new FileInputStream("C:\\java_test\\studentProject\\src\\studentProject\\db.properties");
	    		properties.load(fis);
	    	} catch (FileNotFoundException e) {
	    		System.out.println("FileInputStream error" + e.getMessage());
	    	} catch (IOException e) {
	    		System.out.println("Properties error" + e.getMessage());
	    	}

	    	try {
	         // 1.����̺� �ڵ�
	    		Class.forName(properties.getProperty("driver"));
	         // 2.�����ͺ��̽� ���ӿ�û
	    		connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"),
	               properties.getProperty("password"));
	    	} catch (ClassNotFoundException e) {
	    		System.out.println("Class.forname load error" + e.getStackTrace());
	    	} catch (SQLException e) {
	    		System.out.println("connection error" + e.getMessage());
	    	}
	    }

	   // insert statement
	   public int insert(Student student) {
			PreparedStatement ps = null;
			int insertReturnValue = -1;
//			String insertQuery ="insert into student(no,name,kor,eng,math,total,avr,grade) "+
//							" values(?,?,?,?,?,?,?,?)";
			String insertQuery = "call procedure_insert_studenttbl( ?, ?, ?, ?, ?)";

			try {
			//�Ʒ� ���� ���� �ִ´� Ŀ�ؼǰ����� insert�� �ѰŴ� (prepare �غ��ϴ�)
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, student.getNo());
			ps.setString(2, student.getName());
			ps.setInt(3, student.getKor());
			ps.setInt(4, student.getEng());
			ps.setInt(5, student.getMath());
//			ps.setInt(6, student.getTotal());
//			ps.setDouble(7, student.getAvr());
//			ps.setString(8, student.getGrade());

			//������ ���δ� insert ��(���� ������) �غ��Ű�� �Ʒ��� ����ǥ�� (���ϰ��� �����ϸ�1�����ϸ�0)
			insertReturnValue= ps.executeUpdate();

			} catch (SQLException e1) {
				System.out.println("�����߻�"+e1.getMessage());
			} catch (Exception e) {
				System.out.println("�����߻�"+e.getMessage());
			} finally {
				try {
					if(ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PreparStatement or ResultSet Close Error" + e.getStackTrace());
				}
			}
			return insertReturnValue;
	   }
	   // select statement
	   public List<Student> select() {
			List<Student> list= new ArrayList<Student>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String selectQuery ="select * from studenttbl";

			try {
				//�Ʒ� ���� ���� �ִ´� Ŀ�ؼǰ����� insert�� �ѰŴ� (prepare �غ��ϴ�)
				ps = connection.prepareStatement(selectQuery);

				//������ ���δ� insert ��(���� ������) �غ��Ű�� �Ʒ��� ����ǥ�� (���ϰ��� �����ϸ�1�����ϸ�0)
				//select �����ϸ� ���ϰ��� ResultSet, ������(��ü�⶧����) null�̴� 
				rs = ps.executeQuery(selectQuery);
				//��� ���� ������ üũ
				if(!(rs != null || rs.isBeforeFirst())) {
					return list;
				}
				//rs.next() : ����Ŀ���� �ִ� ���ڵ� ��ġ�� ����.
				while(rs.next()) {
					String no = rs.getString("no");
					String name = rs.getString("name");
					int kor = rs.getInt("kor");
					int eng = rs.getInt("eng");
					int math = rs.getInt("math");
					int total = rs.getInt("total");
					double avr = rs.getDouble("avr");
					String grade = rs.getString("grade");
					int rate = rs.getInt("rate");

					//��ü�� ���� list.add�� �־���Ѵ�
					list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
				}

			} catch (Exception e) {
				System.out.println("select�����߻�"+e.getMessage());
			} finally {
				try {
					if(ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PreparStatement or ResultSet Close Error" + e.getStackTrace());
				}
			}
			return list;
		}
	   // select no or name search statement
	   public List<Student> selectSearch(String data, int type) {
			List<Student> list= new ArrayList<Student>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String selectSearchQuery ="select * from studenttbl where ";

			try {
				switch(type) {
					case 1 : selectSearchQuery += "no like ? "; break;
					case 2 : selectSearchQuery += "name like ? "; break;
					default : System.out.println("�߸��� �Է� Ÿ���Դϴ�");
						return list;
				}
				//�Ʒ� ���� ���� �ִ´� Ŀ�ؼǰ����� insert�� �ѰŴ� (prepare �غ��ϴ�)
				ps = connection.prepareStatement(selectSearchQuery);

				//�˻��Ϸ��� %~~~% �� �־���Ѵ�
				String namePattern = "%"+ data +"%";
				ps.setString(1, namePattern);

				//������ ���δ� insert ��(���� ������) �غ��Ű�� �Ʒ��� ����ǥ�� (���ϰ��� �����ϸ�1�����ϸ�0)
				//select �����ϸ� ���ϰ��� ResultSet, ������(��ü�⶧����) null�̴� 
				rs = ps.executeQuery();
				//��� ���� ������ üũ
				if(!(rs != null || rs.isBeforeFirst())) {
					return list;
				}
				//rs.next() : ����Ŀ���� �ִ� ���ڵ� ��ġ�� ����.
				while(rs.next()) {
					String no = rs.getString("no");
					String name = rs.getString("name");
					int kor = rs.getInt("kor");
					int eng = rs.getInt("eng");
					int math = rs.getInt("math");
					int total = rs.getInt("total");
					double avr = rs.getDouble("avr");
					String grade = rs.getString("grade");
					int rate = rs.getInt("rate");

					//��ü�� ���� list.add�� �־���Ѵ�
					list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
				}

			} catch (Exception e) {
				System.out.println("selectSearch�����߻�"+e.getMessage());
			} finally {
				try {
					if(ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PreparStatement or ResultSet Close Error" + e.getStackTrace());
				}
			}
			return list;
		}
	   // update statement
	   public int update(Student student) {
			PreparedStatement ps = null;
			int updateReturnValue = -1;
			String insertQuery ="UPDATE studenttbl SET kor = ?, eng = ?, math = ?, total = ?, "
					+ "avr = ?, grade = ? where no = ?";

			try {
			//�Ʒ� ���� ���� �ִ´� Ŀ�ؼǰ����� insert�� �ѰŴ� (prepare �غ��ϴ�)
			ps = connection.prepareStatement(insertQuery);
			ps.setInt(1, student.getKor());
			ps.setInt(2, student.getEng());
			ps.setInt(3, student.getMath());
			ps.setInt(4, student.getTotal());
			ps.setDouble(5, student.getAvr());
			ps.setString(6, student.getGrade());
			ps.setString(7, student.getNo());

			//������ ���δ� insert ��(���� ������) �غ��Ű�� �Ʒ��� ����ǥ�� (���ϰ��� �����ϸ�1�����ϸ�0)
			updateReturnValue= ps.executeUpdate();

			} catch (Exception e) {
				System.out.println(" update �����߻�"+e.getMessage());
			} finally {
				try {
					if(ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PreparStatement or ResultSet Close Error" + e.getStackTrace());
				}
			}
			return updateReturnValue;
		}
	   // delete statement
	   public int delete(String no) {
			PreparedStatement ps = null;
			int deleteReturnValue = -1;
			String deleteQuery ="delete from studenttbl where no = ?";

			try {
			//�Ʒ� ���� ���� �ִ´� Ŀ�ؼǰ����� insert�� �ѰŴ� (prepare �غ��ϴ�)
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, no);

			//������ ���δ� insert ��(���� ������) �غ��Ű�� �Ʒ��� ����ǥ�� (���ϰ��� �����ϸ�1�����ϸ�0)
			//���� �����ϸ� ���ϰ��� �����ϸ�1�����ϸ�0
			deleteReturnValue= ps.executeUpdate();

			} catch (Exception e) {
				System.out.println("delete�����߻�"+e.getMessage());
			} finally {
				try {
					if(ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PreparStatement or ResultSet Close Error" + e.getStackTrace());
				}
			}
			return deleteReturnValue;
		}
	   // select order by statement
	   public List<Student> selectOrderBy(int type) {
			List<Student> list= new ArrayList<Student>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String selectOrderByQuery = "select * from studenttbl order by ";

			try {
				switch(type) {
				case 1 : selectOrderByQuery += "no asc "; break;
				case 2 : selectOrderByQuery += "name asc "; break;
				case 3 : selectOrderByQuery += "total desc "; break;
				default : System.out.println("����Ÿ�� ����");
					return list;
				}
				//�Ʒ� ���� ���� �ִ´� Ŀ�ؼǰ����� insert�� �ѰŴ� (prepare �غ��ϴ�)
				ps = connection.prepareStatement(selectOrderByQuery);

				//������ ���δ� insert ��(���� ������) �غ��Ű�� �Ʒ��� ����ǥ�� (���ϰ��� �����ϸ�1�����ϸ�0)
				//select �����ϸ� ���ϰ��� ResultSet, ������(��ü�⶧����) null�̴� 
				rs = ps.executeQuery();
				//��� ���� ������ üũ
				if(!(rs != null || rs.isBeforeFirst())) {
					return list;
				}
				//rs.next() : ����Ŀ���� �ִ� ���ڵ� ��ġ�� ����.
				int rank = 0;
				while(rs.next()) {
					String no = rs.getString("no");
					String name = rs.getString("name");
					int kor = rs.getInt("kor");
					int eng = rs.getInt("eng");
					int math = rs.getInt("math");
					int total = rs.getInt("total");
					double avr = rs.getDouble("avr");
					String grade = rs.getString("grade");
					int rate = rs.getInt("rate");
					if(type == 3) {
						rate = ++rank;
						//���߿� ����Ÿ���̽� rate ������Ʈ�� ���ָ� �ȴ�
					}
					//��ü�� ���� list.add�� �־���Ѵ�
					list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
				}

			} catch (Exception e) {
				System.out.println("select ���� �����߻�"+e.getMessage());
			} finally {
				try {
					if(ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					System.out.println("PreparStatement or ResultSet Close Error" + e.getStackTrace());
				}
			}
			return list;
		}
	   // select Max, Min statement
	   public List<Student> selectMaxMin(int type) {
	      List<Student> list = new ArrayList<Student>();
	      Statement statement = null;
	      ResultSet rs = null;

	      String selectMaxMinQuery = "select * from studenttbl where total = ";
	      try {

	         switch (type) {
	         case 1:
	            selectMaxMinQuery += "(select max(total) from studenttbl)";
	            break;
	         case 2:
	            selectMaxMinQuery += "(select min(total) from studenttbl)";
	            break;
	         default:
	            System.out.println("�Է°� ����");
	            return list;
	         }
	         statement = connection.createStatement();
	         // success -> return 1
	         rs = statement.executeQuery(selectMaxMinQuery);

	         if (!(rs != null || rs.isBeforeFirst())) {
	            return list;
	         }
	         int rank = 0;

	         while (rs.next()) {
	            String no = rs.getString("no");
	            String name = rs.getString("name");
	            int kor = rs.getInt("kor");
	            int eng = rs.getInt("eng");
	            int math = rs.getInt("math");
	            int total = rs.getInt("total");
	            double avr = rs.getDouble("avr");
	            String grade = rs.getString("grade");
	            int rate = rs.getInt("rate");
	            if (type == 3) {
	               rate = ++rank;
	               // later update rate
	            }
	            list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
	         }
	      } catch (Exception e) {
	         System.out.println("select sort error " + e.getMessage());
	      } finally {
	         try {
	            if (statement != null) {
	               statement.close();
	            }
	         } catch (SQLException e) {
	            System.out.println("PreparedStatement close error " + e.getMessage());
	         }
	      }
	      return list;
	   }
	   // Connection Close
	   public void close() {
	      try {
	         if (connection != null) {
	            connection.close();
	         }
	      } catch (SQLException e) {
	         System.out.println("Statement or ResultSet Close Error" + e.getMessage());
	      }
	   }
	}