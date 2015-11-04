package com.billy.hb.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.billy.hb.bean.HeartBeatTab;

/**
 * 心跳表查询dao
 * @author <a href="mailto:li-lei@***.com">li-lei</a>
 * @version $Revision 1.0 $ 2015年1月20日 下午2:22:58
 */
public class HeartBeatDao {
    
    private JdbcTemplate jdbcTemplate;

    private final String seletHeartBeatByAppSql = "select APP_NAME, IDENTITY, UPDATE_TIME, HEARTBEAT_PERIOD, HEARTBEAT_DONE_TIME from heart_beat_tab WHERE ( APP_NAME = ? )";
    
    private final String insertSql = "insert into heart_beat_tab (APP_NAME, IDENTITY, UPDATE_TIME, HEARTBEAT_PERIOD, HEARTBEAT_DONE_TIME) values (?, ?, ?, ?, ?)";
    
    private final String updateHeartBeatTimeSql = "update heart_beat_tab SET UPDATE_TIME = ? WHERE ( APP_NAME = ? and IDENTITY = ? )";
    
    private final String deleteSql = "delete from heart_beat_tab WHERE ( APP_NAME = ? )";
//    @Autowired
//    private HeartBeatTabMapper heartBeatTabMapper;
    
    public List<HeartBeatTab> selectHeartBeatByApp(String appName) {
        List<HeartBeatTab> heartBeatTabs = new ArrayList<HeartBeatTab>();
        List list = jdbcTemplate.queryForList(seletHeartBeatByAppSql, new Object[] {appName});
        HeartBeatTab heartBeatTab = new HeartBeatTab();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Map heartBeat = (Map) iterator.next();
            heartBeatTab = new HeartBeatTab();
            heartBeatTab.setAppName((String) heartBeat.get("APP_NAME"));
            heartBeatTab.setIdentity((String) heartBeat.get("IDENTITY"));
            heartBeatTab.setUpdateTime((String) heartBeat.get("UPDATE_TIME"));
            heartBeatTab.setHeartbeatPeriod((String) heartBeat.get("HEARTBEAT_PERIOD"));
            heartBeatTab.setHeartbeatDoneTime((String) heartBeat.get("HEARTBEAT_DONE_TIME"));
            heartBeatTabs.add(heartBeatTab);
        }
        // HeartBeatTabExample example = new HeartBeatTabExample();
        // example.createCriteria().andAppNameEqualTo(appName);
        // List<HeartBeatTab> heartBeatTabs = heartBeatTabMapper.selectByExample(example);
        return heartBeatTabs;
    }
    
    public String selectSystemDate() {
        return (String) jdbcTemplate.queryForObject("select SYSDATE() from dual", String.class);
        // return heartBeatTabMapper.selectSystemDate();
    }
    
    public void insert(final HeartBeatTab heartBeatTab) {
        jdbcTemplate.update(insertSql, new PreparedStatementSetter() {

            @Override
            public void setValues(java.sql.PreparedStatement ps) throws SQLException {
                ps.setString(1, heartBeatTab.getAppName());
                ps.setString(2, heartBeatTab.getIdentity());
                ps.setString(3, heartBeatTab.getUpdateTime());
                ps.setString(4, heartBeatTab.getHeartbeatPeriod());
                ps.setString(5, heartBeatTab.getHeartbeatDoneTime());
            }

        });
        // heartBeatTabMapper.insert(heartBeatTab);
    }
    
    public void updateHeartBeatTime(final HeartBeatTab heartBeatTab, final String appName,
            final String identity) {
        jdbcTemplate.update(updateHeartBeatTimeSql, new PreparedStatementSetter() {

            @Override
            public void setValues(java.sql.PreparedStatement ps) throws SQLException {
                ps.setString(1, heartBeatTab.getUpdateTime());
                ps.setString(2, appName);
                ps.setString(3, identity);
            }

        });
        // HeartBeatTabExample ex = new HeartBeatTabExample();
        // ex.createCriteria().andAppNameEqualTo(appName).andIdentityEqualTo(identity);
        // heartBeatTabMapper.updateByExampleSelective(heartBeatTab, ex);
    }
    
    public int delete(final String appName) {
        return jdbcTemplate.update(deleteSql, new Object[] {appName});
        // HeartBeatTabExample ex = new HeartBeatTabExample();
        // ex.createCriteria().andAppNameEqualTo(appName);
        // return heartBeatTabMapper.deleteByExample(ex);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
}
