package  io.jenkins.plugins.zookeper;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;

import org.jenkinsci.plugins.workflow.steps.AbstractStepDescriptorImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;




public class ZookeeperReadStep extends AbstractStepImpl {

    private static final long serialVersionUID = 1;

    private String zookeeperNodes;
    private String znode;

    @DataBoundConstructor
    public ZookeeperReadStep(String zookeeperNodes) {
        this.zookeeperNodes = zookeeperNodes;
    }

    @DataBoundSetter
    public void setZnode(String znode) {
        this.znode = znode;
    }

    public String getZookeeperNodes() { return zookeeperNodes; }

    public String getZnode() {
        return znode;
    }

    public ResponseContentSupplier readZnode(Run<?,?> run, TaskListener listener) throws Exception {
        return new ResponseContentSupplier(zookeeperNodes, znode);
    }


    @Override
    public ZookeeperReadStep.DescriptorImpl getDescriptor() {
        return (ZookeeperReadStep.DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends AbstractStepDescriptorImpl {
        public static final String action = "read";

        public DescriptorImpl() {
            super(Execution.class);
        }

        @Override
        public String getFunctionName() {
            return "zookeperRead";
        }

        @Override
        public String getDisplayName() {
            return "Read znode";
        }

    }

    public static final class Execution extends AbstractSynchronousNonBlockingStepExecution<io.jenkins.plugins.zookeper.ResponseContentSupplier> {

        private static final long serialVersionUID = 1;

        @Inject
        private transient ZookeeperReadStep step;

        @StepContextParameter
        private transient Run run;

        @StepContextParameter
        private transient TaskListener listener;

        @Override
        protected io.jenkins.plugins.zookeper.ResponseContentSupplier run() throws Exception {
            return step.readZnode(run, listener);
        }
    }

}
