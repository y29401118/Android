// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.


// This file is autogenerated by
//     base/android/jni_generator/jni_generator.py
// For
//     org/webrtc/SSLCertificateVerifier

#ifndef org_webrtc_SSLCertificateVerifier_JNI
#define org_webrtc_SSLCertificateVerifier_JNI

#include <jni.h>

#include "webrtc/sdk/android/src/jni/jni_generator_helper.h"


// Step 1: Forward declarations.

JNI_REGISTRATION_EXPORT extern const char kClassPath_org_webrtc_SSLCertificateVerifier[];
const char kClassPath_org_webrtc_SSLCertificateVerifier[] = "org/webrtc/SSLCertificateVerifier";
// Leaking this jclass as we cannot use LazyInstance from some threads.
JNI_REGISTRATION_EXPORT std::atomic<jclass> g_org_webrtc_SSLCertificateVerifier_clazz(nullptr);
#ifndef org_webrtc_SSLCertificateVerifier_clazz_defined
#define org_webrtc_SSLCertificateVerifier_clazz_defined
inline jclass org_webrtc_SSLCertificateVerifier_clazz(JNIEnv* env) {
  return base::android::LazyGetClass(env, kClassPath_org_webrtc_SSLCertificateVerifier,
      &g_org_webrtc_SSLCertificateVerifier_clazz);
}
#endif


// Step 2: Constants (optional).


// Step 3: Method stubs.
namespace  webrtc {
namespace jni {


static std::atomic<jmethodID> g_org_webrtc_SSLCertificateVerifier_verify(nullptr);
static jboolean Java_SSLCertificateVerifier_verify(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj, const base::android::JavaRef<jbyteArray>& certificate) {
  jclass clazz = org_webrtc_SSLCertificateVerifier_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      org_webrtc_SSLCertificateVerifier_clazz(env), false);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "verify",
          "([B)Z",
          &g_org_webrtc_SSLCertificateVerifier_verify);

  jboolean ret =
      env->CallBooleanMethod(obj.obj(),
          call_context.base.method_id, certificate.obj());
  return ret;
}

}  // namespace jni
}  // namespace  webrtc

// Step 4: Generated test functions (optional).


#endif  // org_webrtc_SSLCertificateVerifier_JNI
