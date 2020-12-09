package  io.jenkins.plugins.zookeper;

import javax.annotation.Nonnull;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.inject.Inject;


public class ZookeeperWriteStep extends AbstractStepImpl {

    private static final long serialVersionUID = 1;

    private String zookeeperNodes;
    private String znode;
    private String znodeData;


    @DataBoundConstructor
    public ZookeeperWriteStep(String zookeeperNodes) {
        this.zookeeperNodes = zookeeperNodes;
    }

    @DataBoundSetter
    public void setZnode(String znode) {
        this.znode = znode;
    }

    @DataBoundSetter
    public void setZnodeData(String znodeData) {
        this.znodeData = znodeData;
    }

    public String getZookeeperNodes() { return zookeeperNodes; }

    public String getZnode() { return znode; }

    public String getZnodeData() { return znodeData; }

    public ResponseContentSupplier writeZnode(Run<?,?> run, TaskListener listener) throws Exception {
        return new ResponseContentSupplier(zookeeperNodes, znode, znodeData);
    }

    @Override
    public ZookeeperWriteStep.DescriptorImpl getDescriptor() {
        return (ZookeeperWriteStep.DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends AbstractStepDescriptorImpl {
        public DescriptorImpl() {
            super(Execution.class);
        }

        @Override
        public String getFunctionName() {
            return "zookeperWrite";
        }

        @Override
        public String getDisplayName() {
            return "Write znode";
        }

    }

    public static final class Execution extends AbstractSynchronousNonBlockingStepExecution<io.jenkins.plugins.zookeper.ResponseContentSupplier> {

        private static final long serialVersionUID = 1;

        @Inject
        private transient ZookeeperWriteStep step;

        @StepContextParameter
        private transient Run run;

        @StepContextParameter
        private transient TaskListener listener;

        @Override
        protected io.jenkins.plugins.zookeper.ResponseContentSupplier run() throws Exception {
            return step.writeZnode(run, listener);
        }
    }

}
