package io.coodoo.workhorse.ui.api.quarkus.deployment;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;

class WorkhorseUiApiQuarkusProcessor {

    private static final String FEATURE = "workhorse-ui-api-quarkus";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    IndexDependencyBuildItem indexExternalDependency() {
        return new IndexDependencyBuildItem("io.coodoo", "workhorse-ui-api");
    }

    @BuildStep
    public void declareWorkhorseAsBean(CombinedIndexBuildItem index, BuildProducer<AdditionalBeanBuildItem> additionalBeans) {

        // All cdi-Beans are here registered to be available in quarkus application
        List<String> workhorseBeans = index.getIndex().getKnownClasses().stream().filter(ci -> !Modifier.isAbstract(ci.flags())).map(ci -> ci.name().toString())
                        .filter(c -> c.startsWith("io.coodoo.workhorse.api.")).collect(Collectors.toList());

        additionalBeans.produce(
                        new AdditionalBeanBuildItem.Builder().addBeanClasses(workhorseBeans).setUnremovable().setDefaultScope(DotNames.DEFAULT_BEAN).build());
    }

}
