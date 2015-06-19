package net.hfutonline.horm.bean;

/**
 * 管理配置信息
 * 
 * @author zlb
 *
 */
public class Configuration {
	/**
	 * 驱动类
	 */
	private String driver;
	/**
	 * jdbc的url
	 */
	private String url;
	/**
	 * 数据库的用户名
	 */
	private String user;
	/**
	 * 数据库的密码
	 */
	private String pwd;
	/**
	 * 正在使用的数据库
	 */
	private String usingdb;
	/**
	 * 项目的源码路径
	 */
	private String srcPath;
	/**
	 * 扫描生成java类的包(po:persistence object)
	 */
	private String poPackage;
	/**
	 * 项目使用的查询类的路径
	 */
	private String queryClass;
	/**
	 * 连接池中最小连接数 
	 */
	private Integer poolMinSize;
	/**
	 * 连接池中最大连接数 
	 */
	private Integer poolMaxSize;
	
	public Configuration() {
	}

	public Configuration(String driver, String url, String user, String pwd,
			String usingdb, String srcPath, String poPackage,
			String queryClass, Integer poolMinSize, Integer poolMaxSize) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.usingdb = usingdb;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
		this.queryClass = queryClass;
		this.poolMinSize = poolMinSize;
		this.poolMaxSize = poolMaxSize;
	}



	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUsingdb() {
		return usingdb;
	}

	public void setUsingdb(String usingdb) {
		this.usingdb = usingdb;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getPoPackage() {
		return poPackage;
	}

	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}

	public String getQueryClass() {
		return queryClass;
	}

	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}

	public Integer getPoolMinSize() {
		return poolMinSize;
	}

	public void setPoolMinSize(Integer poolMinSize) {
		this.poolMinSize = poolMinSize;
	}

	public Integer getPoolMaxSize() {
		return poolMaxSize;
	}

	public void setPoolMaxSize(Integer poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
	}

}
