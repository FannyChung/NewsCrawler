
/**
 * ��Ʒ��url����ͬ����Ʒ��url���в�ͬ��hashֵ
 * 
 * @author Fanny
 * 
 */
public class NewsUrl {
	/**
	 * ���ŵ�url
	 */
	private String string;

	/**
	 * @param s
	 */
	public NewsUrl(String s) {
		string = s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		NewsUrl other = (NewsUrl) arg0;
		String str1 = string.split("/")[6];
		String str2 = other.string.split("/")[6];// ����/�ָ��ĵ�����Ԫ�ر�����ʶ��ͬ��Ʒ
		if (str1.equals(str2))
			return true;
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		String str1 = string.split("/")[6];// ����/�ָ��ĵ��߸�Ԫ�ر�����ʶ��ͬ��Ʒ
		return new String(str1).hashCode();
	}

	/**
	 * @return url
	 */
	public String getString() {
		return string;
	}
}
