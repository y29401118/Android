/*
 *  Copyright (c) 2020 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

#ifndef API_VOIP_VOIP_VOLUME_CONTROL_H_
#define API_VOIP_VOIP_VOLUME_CONTROL_H_

#include "api/voip/voip_base.h"

namespace webrtc {

struct VolumeInfo {
  // https://w3c.github.io/webrtc-stats/#dom-rtcaudiosourcestats-audiolevel
  double audio_level = 0;
  // https://w3c.github.io/webrtc-stats/#dom-rtcaudiosourcestats-totalaudioenergy
  double total_energy = 0.0;
  // https://w3c.github.io/webrtc-stats/#dom-rtcaudiosourcestats-totalsamplesduration
  double total_duration = 0.0;
};

// VoipVolumeControl interface.
//
// This sub-API supports functions related to the input (microphone) and output
// (speaker) device.
//
// Caller must ensure that ChannelId is valid otherwise it will result in no-op
// with error logging.
class VoipVolumeControl {
 public:
  // Mute/unmutes the microphone input sample before encoding process. Note that
  // mute doesn't affect audio input level and energy values as input sample is
  // silenced after the measurement.
  virtual void SetInputMuted(ChannelId channel_id, bool enable) = 0;

  // Gets the microphone volume info.
  // Returns absl::nullopt if |channel_id| is invalid.
  virtual absl::optional<VolumeInfo> GetInputVolumeInfo(
      ChannelId channel_id) = 0;

  // Gets the speaker volume info.
  // Returns absl::nullopt if |channel_id| is invalid.
  virtual absl::optional<VolumeInfo> GetOutputVolumeInfo(
      ChannelId channel_id) = 0;

 protected:
  virtual ~VoipVolumeControl() = default;
};

}  // namespace webrtc

#endif  // API_VOIP_VOIP_VOLUME_CONTROL_H_
