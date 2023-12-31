// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef BASE_PROFILER_THREAD_DELEGATE_POSIX_H_
#define BASE_PROFILER_THREAD_DELEGATE_POSIX_H_

#include "base/base_export.h"
#include "base/profiler/sampling_profiler_thread_token.h"
#include "base/profiler/thread_delegate.h"
#include "base/threading/platform_thread.h"

namespace base {

// Platform- and thread-specific implementation in support of stack sampling on
// POSIX.
class BASE_EXPORT ThreadDelegatePosix : public ThreadDelegate {
 public:
  ThreadDelegatePosix(SamplingProfilerThreadToken thread_token);

  ThreadDelegatePosix(const ThreadDelegatePosix&) = delete;
  ThreadDelegatePosix& operator=(const ThreadDelegatePosix&) = delete;

  // ThreadDelegate
  PlatformThreadId GetThreadId() const override;
  uintptr_t GetStackBaseAddress() const override;
  std::vector<uintptr_t*> GetRegistersToRewrite(
      RegisterContext* thread_context) override;

 private:
  const PlatformThreadId thread_id_;
  const uintptr_t thread_stack_base_address_;
};

}  // namespace base

#endif  // BASE_PROFILER_THREAD_DELEGATE_POSIX_H_
