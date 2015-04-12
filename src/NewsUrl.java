
/**
 * 商品的url，不同的商品的url具有不同的hash值
 * 
 * @author Fanny
 * 
 */
public class NewsUrl {
	/**
	 * 新闻的url
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
		String str2 = other.string.split("/")[6];// 根据/分割后的第六个元素编码来识别不同商品
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
		String str1 = string.split("/")[6];// 根据/分割后的第七个元素编码来识别不同商品
		return new String(str1).hashCode();
	}

	/**
	 * @return url
	 */
	public String getString() {
		return string;
	}
}
