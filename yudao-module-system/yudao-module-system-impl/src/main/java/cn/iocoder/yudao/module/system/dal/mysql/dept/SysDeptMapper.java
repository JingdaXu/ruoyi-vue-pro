package cn.iocoder.yudao.module.system.dal.mysql.dept;

import cn.iocoder.yudao.framework.mybatis.core.enums.SqlConstants;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.yudao.coreservice.modules.system.dal.dataobject.dept.SysDeptDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SysDeptMapper extends BaseMapperX<SysDeptDO> {

    default List<SysDeptDO> selectList(DeptListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SysDeptDO>().likeIfPresent(SysDeptDO::getName, reqVO.getName())
                .eqIfPresent(SysDeptDO::getStatus, reqVO.getStatus()));
    }

    default SysDeptDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(new LambdaQueryWrapper<SysDeptDO>().eq(SysDeptDO::getParentId, parentId)
                .eq(SysDeptDO::getName, name));
    }

    default Integer selectCountByParentId(Long parentId) {
        return selectCount(SysDeptDO::getParentId, parentId);
    }

    @InterceptorIgnore(tenantLine = "on") // 该方法忽略多租户。原因：该方法被异步 task 调用，此时获取不到租户编号
    default boolean selectExistsByUpdateTimeAfter(Date maxUpdateTime) {
        return selectOne(new LambdaQueryWrapper<SysDeptDO>().select(SysDeptDO::getId)
                .gt(SysDeptDO::getUpdateTime, maxUpdateTime).last(SqlConstants.LIMIT1)) != null;
    }

}
