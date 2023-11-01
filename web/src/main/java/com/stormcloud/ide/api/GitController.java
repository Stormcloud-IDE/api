package com.stormcloud.ide.api;

/*
 * #%L
 * Stormcloud IDE - API - Web
 * %%
 * Copyright (C) 2012 - 2013 Stormcloud IDE
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import com.stormcloud.ide.api.git.IGitManager;
import com.stormcloud.ide.api.git.exception.GitManagerException;
import com.stormcloud.ide.api.git.model.IndexState;
import com.stormcloud.ide.model.git.Add;
import com.stormcloud.ide.model.git.Clone;
import com.stormcloud.ide.model.git.Commit;
import com.stormcloud.ide.model.git.Log;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author martijn
 */
@Controller
@RequestMapping(value = "/git")
public class GitController extends BaseController {

    private Logger LOG = Logger.getLogger(getClass());
    @Autowired
    private IGitManager manager;

    @RequestMapping(value = "/clone-remote",
    method = RequestMethod.POST,
    consumes = "application/json")
    @ResponseBody
    public String cloneRemoteRepository(
            @RequestBody Clone clone)
            throws GitManagerException {

        LOG.info("Clone request for uri : " + clone.getUri());

        return manager.cloneRemoteRepository(clone.getUri());
    }

    @RequestMapping(value = "/indexState",
    method = RequestMethod.GET)
    @ResponseBody
    public IndexState list(
            @RequestParam(value = "filePath", required = true) String filePath)
            throws GitManagerException {

        return manager.getIndexState(filePath);
    }

    @RequestMapping(value = "/status",
    method = RequestMethod.GET)
    @ResponseBody
    public String status(
            /**
             * @todo remove project parameter and fix this /filesystem/martijn
             * thing
             */
            @RequestParam(value = "project", required = true) String project,
            @RequestParam(value = "filePath", required = true) String filePath)
            throws GitManagerException {

        return manager.getStatus(filePath, "/filesystem/martijn");
    }

    @RequestMapping(value = "/commit",
    method = RequestMethod.POST,
    consumes = "application/json")
    @ResponseBody
    public void commit(
            @RequestBody Commit commit)
            throws GitManagerException {

        LOG.info("Git commit " + commit.getRepository());

        manager.commit(
                commit.getRepository(),
                commit.getMessage(),
                commit.getFiles(),
                commit.isAll());
    }

    @RequestMapping(value = "/add",
    method = RequestMethod.POST,
    consumes = "application/json")
    @ResponseBody
    public int add(
            @RequestBody Add add)
            throws GitManagerException {

        manager.add(add.getRepository(), add.getPattern());

        return 0;
    }

    @RequestMapping(value = "/log",
    method = RequestMethod.POST,
    consumes = "application/json",
    produces = "application/json")
    @ResponseBody
    public void log(
            @RequestBody Log log)
            throws GitManagerException {

        LOG.info("Fetch Log for " + log.getRepository());

        manager.log(log.getRepository());

        //return result;
    }
}
