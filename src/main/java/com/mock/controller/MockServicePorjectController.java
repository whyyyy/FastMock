package com.mock.controller;

import com.mock.bean.MockResult;
import com.mock.mapper.MockProjectMapper;
import com.mock.mapper.MockUriMapper;
import com.mock.model.MockProject;
import com.mock.model.MockUri;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mock/project")
@RestController
public class MockServicePorjectController {

    @Autowired
    private MockProjectMapper mockProjectMapper;
    @Autowired
    private MockUriMapper mockUriMapper;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public MockResult insert(@RequestBody MockProject p) {
        p.setId(UUID.randomUUID().toString());
        int count = mockProjectMapper.insert(p);
        MockResult rs;
        if (count == 1) {
            rs = new MockResult<>(MockResult.SUCCESS, p.getId());
        } else {
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MockResult get(@PathVariable String id) {
        MockProject p = new MockProject();
        p.setId(id);
        MockResult rs;
        List<MockProject> ls = mockProjectMapper.select(p);
        if (ls.size() > 0) {
            rs = new MockResult<>(MockResult.SUCCESS, ls);
        }else{
            rs = new MockResult(MockResult.SUCCESS);
        }
        return rs;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public MockResult getALL() {
        MockResult rs;
        List<MockProject> ls = mockProjectMapper.select(null);
        if (ls.size() > 0) {
            rs = new MockResult<>(MockResult.SUCCESS, ls);
        }else{
            rs = new MockResult(MockResult.SUCCESS);
        }
        return rs;
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public MockResult save(@RequestBody MockProject p) {
        int count = mockProjectMapper.update(p);
        MockResult rs;
        if (count == 1) {
            rs = new MockResult(MockResult.SUCCESS);
        } else {
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public MockResult delete(@RequestBody MockProject p) {
        MockResult rs;
        MockUri mockUriBean = new MockUri();
        mockUriBean.setMockProjectId(p.getId());

        if (mockUriMapper.select(mockUriBean).size() > 0) {
            rs = new MockResult<>(MockResult.ERROR, "please delete the Uri items", null);
            return rs;
        }

        int count = mockProjectMapper.delete(p);
        if (count == 1) {
            rs = new MockResult(MockResult.SUCCESS);
        } else {
            rs = new MockResult(MockResult.ERROR);
        }
        return rs;
    }

}
