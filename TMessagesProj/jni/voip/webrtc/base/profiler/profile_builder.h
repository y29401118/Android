// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef BASE_PROFILER_PROFILE_BUILDER_H_
#define BASE_PROFILER_PROFILE_BUILDER_H_

#include <memory>

#include "base/base_export.h"
#include "base/optional.h"
#include "base/profiler/frame.h"
#include "base/profiler/module_cache.h"
#include "base/time/time.h"

namespace base {

// The ProfileBuilder interface allows the user to record profile information on
// the fly in whatever format is desired. Functions are invoked by the profiler
// on its own thread so must not block or perform expensive operations.
class BASE_EXPORT ProfileBuilder {
 public:
  ProfileBuilder() = default;
  virtual ~ProfileBuilder() = default;

  // Gets the ModuleCache to be used by the StackSamplingProfiler when looking
  // up modules from addresses.
  virtual ModuleCache* GetModuleCache() = 0;

  struct BASE_EXPORT MetadataItem {
    MetadataItem(uint64_t name_hash, Optional<int64_t> key, int64_t value);
    MetadataItem();

    MetadataItem(const MetadataItem& other);
    MetadataItem& operator=(const MetadataItem& other);

    // The hash of the metadata name, as produced by HashMetricName().
    uint64_t name_hash;
    // The key if specified when setting the item.
    Optional<int64_t> key;
    // The value of the metadata item.
    int64_t value;
  };

  static constexpr size_t MAX_METADATA_COUNT = 50;
  typedef std::array<MetadataItem, MAX_METADATA_COUNT> MetadataItemArray;

  class MetadataProvider {
   public:
    MetadataProvider() = default;
    virtual ~MetadataProvider() = default;

    virtual size_t GetItems(ProfileBuilder::MetadataItemArray* const items) = 0;
  };

  // Records metadata to be associated with the current sample. To avoid
  // deadlock on locks taken by the suspended profiled thread, implementations
  // of this method must not execute any code that could take a lock, including
  // heap allocation or use of CHECK/DCHECK/LOG statements. Generally
  // implementations should simply atomically copy metadata state to be
  // associated with the sample.
  virtual void RecordMetadata(MetadataProvider* metadata_provider) {}

  // Applies the specified metadata |item| to samples collected in the range
  // [period_start, period_end), iff the profile already captured execution that
  // covers that range entirely. This restriction avoids bias in the results
  // towards samples in the middle of the period, at the expense of excluding
  // periods overlapping the start or end of the profile. |period_end| must be
  // <= TimeTicks::Now().
  virtual void ApplyMetadataRetrospectively(TimeTicks period_start,
                                            TimeTicks period_end,
                                            const MetadataItem& item) {}

  // Records a new set of frames. Invoked when sampling a sample completes.
  virtual void OnSampleCompleted(std::vector<Frame> frames,
                                 TimeTicks sample_timestamp) = 0;

  // Finishes the profile construction with |profile_duration| and
  // |sampling_period|. Invoked when sampling a profile completes.
  virtual void OnProfileCompleted(TimeDelta profile_duration,
                                  TimeDelta sampling_period) = 0;

 private:
  DISALLOW_COPY_AND_ASSIGN(ProfileBuilder);
};

}  // namespace base

#endif  // BASE_PROFILER_PROFILE_BUILDER_H_
