package com.wisematch.modules.chat.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 用户在线简历
 * @author yongliang.j
 * @version UserResume.java, v0.1 2025-07-24 09:52
 */
@Data
@Slf4j
public class UserResumeOnline {

    String id;

    Integer score;

    @Schema(description = "完整度评分")
    private Integer completenessScore = 10;

    /**
     * 人才简介
     */
    @Schema(description = "人才简介")
    PortraitBrief brief = new PortraitBrief();

    /**
     * 求职意向
     */
    @Schema(description = "求职意向")
    List<JSONObject> jobIntention;

    /**
     * 工作经历
     */
    @Schema(description = "工作经历")
    List<Experience> workExperience;

    /**
     * 教育经历
     */
    @Schema(description = "教育经历")
    List<EduExperience> eduExperience;

    /**
     * 项目经理
     */
    @Schema(description = "项目经理")
    List<Experience> projectExperience;

    /**
     * 在校经历
     */
    @Schema(description = "在校经历")
    List<Experience> schoolExperience;

    /**
     * 附件信息
     */
    @Schema(description = "附件信息")
    String extraInfo;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "微信号")
    private String vxNo;

    @Schema(description = "意向城市")
    private String base;

    @Schema(description = "个人优势")
    private String advantages;

    public static Integer completenessScore(JSONObject jsonObject) {
        int completenessScore = 0;
        try {
            UserResumeOnline resumeDTO = JSONObject.toJavaObject(jsonObject, UserResumeOnline.class);
            PortraitBrief brief = resumeDTO.getBrief();
            if(brief != null){
                if (StringUtils.isNotBlank(brief.getUserPhoto())) {
                    completenessScore += 5;
                }
                if (brief.getExtra() != null) {
                    String graduationTime = brief.getExtra().getString("graduationTime");
                    String workingTime = brief.getExtra().getString("workingTime");
                    if (StringUtils.isNotBlank(graduationTime) || StringUtils.isNotBlank(workingTime)) {
                        completenessScore += 10;
                    }
                }
                if (StringUtils.isNotBlank(brief.getSkills())) {
                    completenessScore += 10;
                }
                if (StringUtils.isNotBlank(brief.getUserBrief())) {
                    completenessScore += 5;
                }
            }

            List<Experience> workExperience = resumeDTO.getWorkExperience();
            if (!CollectionUtils.isEmpty(workExperience)) {

                completenessScore += 20;
            }
            List<Experience> projectExperience = resumeDTO.getProjectExperience();
            if (!CollectionUtils.isEmpty(projectExperience)) {
                completenessScore += 15;
            }
            List<EduExperience> eduExperience = resumeDTO.getEduExperience();
            if (!CollectionUtils.isEmpty(eduExperience)) {
                completenessScore += 15;
            }
            List<Experience> schoolExperience = resumeDTO.getSchoolExperience();
            if (!CollectionUtils.isEmpty(schoolExperience)) {
                completenessScore += 10;
            }
            String extraInfo = resumeDTO.getExtraInfo();
            if (StringUtils.isNotBlank(extraInfo)) {
                completenessScore += 10;
            }
        } catch (Exception e) {
            log.error("简历完整度计算异常", e);
        }
        return completenessScore;
    }


}






/**
 * 经历
 */
@Data
class Experience {

    /**
     * 公司、学校、项目
     */
    @Schema(description = "公司、学校、项目")
    String name;

    /**
     * 岗位、专业、角色
     */
    @Schema(description = "岗位、专业、角色")
    String label;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间")
    String startDate;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间")
    String endDate;

    /**
     * 经历内容
     */
    @Schema(description = "经历内容")
    String content;
}

/**
 * 教育经历
 */
@Data
class EduExperience extends Experience {

    /**
     * 学历水平
     */
    @Schema(description = "学历水平")
    String degree;
}

