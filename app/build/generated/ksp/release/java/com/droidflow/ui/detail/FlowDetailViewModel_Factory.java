package com.droidflow.ui.detail;

import androidx.lifecycle.SavedStateHandle;
import com.droidflow.data.local.FlowRepository;
import com.droidflow.domain.engine.FlowEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class FlowDetailViewModel_Factory implements Factory<FlowDetailViewModel> {
  private final Provider<FlowRepository> repositoryProvider;

  private final Provider<FlowEngine> flowEngineProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public FlowDetailViewModel_Factory(Provider<FlowRepository> repositoryProvider,
      Provider<FlowEngine> flowEngineProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.flowEngineProvider = flowEngineProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public FlowDetailViewModel get() {
    return newInstance(repositoryProvider.get(), flowEngineProvider.get(), savedStateHandleProvider.get());
  }

  public static FlowDetailViewModel_Factory create(Provider<FlowRepository> repositoryProvider,
      Provider<FlowEngine> flowEngineProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new FlowDetailViewModel_Factory(repositoryProvider, flowEngineProvider, savedStateHandleProvider);
  }

  public static FlowDetailViewModel newInstance(FlowRepository repository, FlowEngine flowEngine,
      SavedStateHandle savedStateHandle) {
    return new FlowDetailViewModel(repository, flowEngine, savedStateHandle);
  }
}
