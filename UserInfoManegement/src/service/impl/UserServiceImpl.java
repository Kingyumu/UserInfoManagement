package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import domain.PageBean;
import domain.User;
import service.UserService;

import java.util.List;
import java.util.Map;

public  class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();

    @Override
    public List<User> findAll() {
        //调用Dao完成查询
        return dao.findAll();
    }

    @Override
    public User login(String username, String password) {
        return dao.findUserByUsernameAndPassword(username, password);
    }

    @Override
    public void addUser(User user) {
        dao.add(user);
    }

    @Override
    public void deleteUser(String id) {
        dao.delete(Integer.parseInt(id));
    }

    @Override
    public User findUserById(String id) {
        return dao.findById(Integer.parseInt(id));
    }

    @Override
    public void updateUser(User user) {
        dao.update(user);
    }

    @Override
    public void delSelectedUser(String[] ids) {
        if (ids != null && ids.length > 0){
            //遍历数组
            for (String id : ids){
                //调用删除
                dao.delete(Integer.parseInt(id));
            }
        }
    }

    @Override
    public void addRegisterInfo(String username, String password, String email) {
        dao.addUsernameAndPasswordAndEmail(username, password, email);
    }

    @Override
    public User findUserByUsername(String username) {
        return dao.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public PageBean<User> findUserByPage(String _currentPage, String _rows, Map<String, String[]> condition) {
        int currentPage = Integer.parseInt(_currentPage);
        int rows = Integer.parseInt(_rows);

        if (currentPage <= 0){
            currentPage = 1;
        }

        //创建空的PageBean对象
        PageBean<User> pb = new PageBean<User>();

        //调用dao查询总记录数
        int totalCount = dao.findTotalCount(condition);
        if (totalCount == -1){
            return null;
        }

        //计算总页码
        int totalPage = (totalCount % rows) == 0 ? totalCount / rows : (totalCount/rows) + 1;

        if (currentPage >= totalPage){
            currentPage = totalPage;
        }

        //调用dao查询List集合
        //计算开始的记录索引
        int start = (currentPage - 1) * rows;
        List<User> list = dao.findByPage(start, rows, condition);
        if (list == null){
            return null;
        }

        //设置参数
        pb.setRows(rows);
        pb.setCurrentPage(currentPage);
        pb.setTotalPage(totalPage);
        pb.setTotalCount(totalCount);
        pb.setList(list);

        return pb;
    }
}
