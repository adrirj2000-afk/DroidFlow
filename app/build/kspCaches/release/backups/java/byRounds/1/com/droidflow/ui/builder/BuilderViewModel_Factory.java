package com.droidflow.ui.builder;

import androidx.lifecycle.SavedStateHandle;
import com.droidflow.data.local.FlowRepository;
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
public final class BuilderViewModel_Factory implements Factory<BuilderViewModel> {
  private final Provider<FlowRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public BuilderViewModel_Factory(Provider<FlowRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public BuilderViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static BuilderViewModel_Factory create(Provider<FlowRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new BuilderViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static BuilderViewModel newInstance(FlowRepository repository,
      SavedStateHandle savedStateHandle) {
    return new BuilderViewModel(repository, savedStateHandle);
  }
}
