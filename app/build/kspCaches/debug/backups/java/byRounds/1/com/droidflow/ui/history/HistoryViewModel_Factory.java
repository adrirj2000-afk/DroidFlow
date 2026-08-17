package com.droidflow.ui.history;

import com.droidflow.core.preferences.PreferencesManager;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<FlowRepository> repositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public HistoryViewModel_Factory(Provider<FlowRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(repositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static HistoryViewModel_Factory create(Provider<FlowRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new HistoryViewModel_Factory(repositoryProvider, preferencesManagerProvider);
  }

  public static HistoryViewModel newInstance(FlowRepository repository,
      PreferencesManager preferencesManager) {
    return new HistoryViewModel(repository, preferencesManager);
  }
}
