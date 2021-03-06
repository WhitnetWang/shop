package cn.itcast.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.category.domain.Category;
import cn.itcast.bookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();

	/*
	 * 查询所有
	 */
	public List<Book> findAll() {
		try {
			String sql = "select * from book where del = false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 按分类查找
	 */
	public List<Book> findByCategory(String cid) {
		try {
			String sql = "select * from book where cid = ? and del = false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 按bid加载
	 */
	public Book load(String bid) {
		try {
			String sql = "select * from book where bid = ?";
			Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
			Category category = CommonUtils.toBean(map, Category.class);
			Book book = CommonUtils.toBean(map, Book.class);
			book.setCategory(category);
			return book;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 获取某分类下的所有图书的数量
	 */
	public int getCountByCid(String cid) {
		try {
			String sql = "select count(*) from book where cid = ?";
			Number cnt = (Number) qr.query(sql, new ScalarHandler(), cid);
			return cnt.intValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 添加图书
	 */
	public void add(Book book) {
		try {
			String sql = "insert into book values (?,?,?,?,?,?,?)";
			Object[] params = { book.getBid(), book.getBname(),
					book.getPrice(), book.getAuthor(), book.getImage(),
					book.getCategory().getCid(),book.isDel() };
			qr.update(sql, params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 删除图书
	 */
	public void delete(String bid) {
		try {
			String sql = "update book set del = true where bid = ?";
			qr.update(sql, bid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 修改
	 */
	public void edit(Book book) {
		try {
			String sql = "update book set bname = ?, price = ?, author = ?, cid = ? where bid = ?";
			Object[] params = { book.getBname(), book.getPrice(),
					book.getAuthor(), book.getCategory().getCid(),book.getBid() };
			qr.update(sql, params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
