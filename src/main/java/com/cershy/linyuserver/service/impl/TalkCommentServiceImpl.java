package com.cershy.linyuserver.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cershy.linyuserver.dto.CommentListDto;
import com.cershy.linyuserver.entity.Talk;
import com.cershy.linyuserver.entity.TalkComment;
import com.cershy.linyuserver.mapper.TalkCommentMapper;
import com.cershy.linyuserver.service.TalkCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cershy.linyuserver.service.TalkService;
import com.cershy.linyuserver.vo.talkComment.CreateTalkCommentVo;
import com.cershy.linyuserver.vo.talkComment.DeleteTalkCommentVo;
import com.cershy.linyuserver.vo.talkComment.TalkCommentListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 说说评论 服务实现类
 * </p>
 *
 * @author heath
 * @since 2024-07-03
 */
@Service
public class TalkCommentServiceImpl extends ServiceImpl<TalkCommentMapper, TalkComment> implements TalkCommentService {

    @Resource
    TalkCommentMapper talkCommentMapper;

    @Resource
    TalkService talkService;

    @Override
    public boolean createTalkComment(String userId, CreateTalkCommentVo createTalkCommentVo) {

        Talk talk = talkService.getById(createTalkCommentVo.getTalkId());
        talk.setCommentNum(talk.getCommentNum() + 1);
        talkService.updateById(talk);

        TalkComment talkComment = new TalkComment();
        talkComment.setId(IdUtil.randomUUID());
        talkComment.setTalkId(createTalkCommentVo.getTalkId());
        talkComment.setUserId(userId);
        talkComment.setContent(createTalkCommentVo.getComment());

        return save(talkComment);
    }

    @Override
    public List<CommentListDto> talkCommentList(String userId, TalkCommentListVo talkCommentListVo) {
        List<CommentListDto> list = talkCommentMapper.talkCommentList(userId, talkCommentListVo.getTalkId());
        return list;
    }

    @Override
    public boolean deleteTalkComment(String userId, DeleteTalkCommentVo deleteTalkLikeVo) {
        Talk talk = talkService.getById(deleteTalkLikeVo.getTalkId());
        talk.setCommentNum(talk.getCommentNum() - 1);
        talkService.updateById(talk);
        LambdaQueryWrapper<TalkComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TalkComment::getTalkId, deleteTalkLikeVo.getTalkId())
                .eq(TalkComment::getUserId, userId);
        return remove(queryWrapper);
    }
}
