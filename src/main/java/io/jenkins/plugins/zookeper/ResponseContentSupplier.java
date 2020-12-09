package io.jenkins.plugins.zookeper;

import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted;


import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;


public class ResponseContentSupplier implements Serializable {

    private static final long serialVersionUID = 1;

    private String znodeData;

    public ResponseContentSupplier(String zookeeperNodes, String znode, String znodeData) throws Exception {

        CuratorFramework zookeeperClient = CuratorFrameworkFactory.newClient(zookeeperNodes, new ExponentialBackoffRetry(1000, 3));
        zookeeperClient.start();
        this.znodeData = znodeData;

        if ( Objects.isNull(zookeeperClient.checkExists().forPath(znode)) ) {
            zookeeperClient.create().creatingParentsIfNeeded().forPath(znode, znodeData.getBytes(Charset.defaultCharset()));
        } else {
            zookeeperClient.setData().forPath(znode, znodeData.getBytes(Charset.defaultCharset()));
        }
        zookeeperClient.close();
    }

    public ResponseContentSupplier(String zookeeperNodes, String znode) throws Exception {
        CuratorFramework zookeeperClient = CuratorFrameworkFactory.newClient(zookeeperNodes, new ExponentialBackoffRetry(1000, 3));
        zookeeperClient.start();
        znodeData = new String(zookeeperClient.getData().forPath(znode), Charset.defaultCharset());
        zookeeperClient.close();
    }

    @Whitelisted
    public String getZnodeData() {
        return znodeData;
    }


    @Override
    public String toString() {
        return znodeData;
    }
}