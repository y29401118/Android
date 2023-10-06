// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.


// This file is autogenerated by
//     base/android/jni_generator/jni_generator.py
// For
//     java/util/LinkedHashMap

#ifndef java_util_LinkedHashMap_JNI
#define java_util_LinkedHashMap_JNI

#include <jni.h>

#include "webrtc/sdk/android/src/jni/jni_generator_helper.h"


// Step 1: Forward declarations.

JNI_REGISTRATION_EXPORT extern const char kClassPath_java_util_LinkedHashMap[];
const char kClassPath_java_util_LinkedHashMap[] = "java/util/LinkedHashMap";
// Leaking this jclass as we cannot use LazyInstance from some threads.
JNI_REGISTRATION_EXPORT std::atomic<jclass> g_java_util_LinkedHashMap_clazz(nullptr);
#ifndef java_util_LinkedHashMap_clazz_defined
#define java_util_LinkedHashMap_clazz_defined
inline jclass java_util_LinkedHashMap_clazz(JNIEnv* env) {
  return base::android::LazyGetClass(env, kClassPath_java_util_LinkedHashMap,
      &g_java_util_LinkedHashMap_clazz);
}
#endif


// Step 2: Constants (optional).


// Step 3: Method stubs.
namespace JNI_LinkedHashMap {


static std::atomic<jmethodID> g_java_util_LinkedHashMap_containsValue(nullptr);
static jboolean Java_LinkedHashMap_containsValue(JNIEnv* env, const base::android::JavaRef<jobject>&
    obj, const base::android::JavaRef<jobject>& p0) __attribute__ ((unused));
static jboolean Java_LinkedHashMap_containsValue(JNIEnv* env, const base::android::JavaRef<jobject>&
    obj, const base::android::JavaRef<jobject>& p0) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env), false);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "containsValue",
          "(Ljava/lang/Object;)Z",
          &g_java_util_LinkedHashMap_containsValue);

  jboolean ret =
      env->CallBooleanMethod(obj.obj(),
          call_context.base.method_id, p0.obj());
  return ret;
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_get(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_get(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj, const base::android::JavaRef<jobject>& p0) __attribute__
    ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_get(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj, const base::android::JavaRef<jobject>& p0) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "get",
          "(Ljava/lang/Object;)Ljava/lang/Object;",
          &g_java_util_LinkedHashMap_get);

  jobject ret =
      env->CallObjectMethod(obj.obj(),
          call_context.base.method_id, p0.obj());
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_getOrDefault(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_getOrDefault(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj, const base::android::JavaRef<jobject>& p0,
    const base::android::JavaRef<jobject>& p1) __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_getOrDefault(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj, const base::android::JavaRef<jobject>& p0,
    const base::android::JavaRef<jobject>& p1) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "getOrDefault",
          "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
          &g_java_util_LinkedHashMap_getOrDefault);

  jobject ret =
      env->CallObjectMethod(obj.obj(),
          call_context.base.method_id, p0.obj(), p1.obj());
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_clear(nullptr);
static void Java_LinkedHashMap_clear(JNIEnv* env, const base::android::JavaRef<jobject>& obj)
    __attribute__ ((unused));
static void Java_LinkedHashMap_clear(JNIEnv* env, const base::android::JavaRef<jobject>& obj) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env));

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "clear",
          "()V",
          &g_java_util_LinkedHashMap_clear);

     env->CallVoidMethod(obj.obj(),
          call_context.base.method_id);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_removeEldestEntry(nullptr);
static jboolean Java_LinkedHashMap_removeEldestEntry(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj, const base::android::JavaRef<jobject>& p0) __attribute__
    ((unused));
static jboolean Java_LinkedHashMap_removeEldestEntry(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj, const base::android::JavaRef<jobject>& p0) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env), false);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "removeEldestEntry",
          "(Ljava/util/Map$Entry;)Z",
          &g_java_util_LinkedHashMap_removeEldestEntry);

  jboolean ret =
      env->CallBooleanMethod(obj.obj(),
          call_context.base.method_id, p0.obj());
  return ret;
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_keySet(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_keySet(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj) __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_keySet(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "keySet",
          "()Ljava/util/Set;",
          &g_java_util_LinkedHashMap_keySet);

  jobject ret =
      env->CallObjectMethod(obj.obj(),
          call_context.base.method_id);
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_values(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_values(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj) __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_values(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "values",
          "()Ljava/util/Collection;",
          &g_java_util_LinkedHashMap_values);

  jobject ret =
      env->CallObjectMethod(obj.obj(),
          call_context.base.method_id);
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_entrySet(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_entrySet(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj) __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_entrySet(JNIEnv* env, const
    base::android::JavaRef<jobject>& obj) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "entrySet",
          "()Ljava/util/Set;",
          &g_java_util_LinkedHashMap_entrySet);

  jobject ret =
      env->CallObjectMethod(obj.obj(),
          call_context.base.method_id);
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_forEach(nullptr);
static void Java_LinkedHashMap_forEach(JNIEnv* env, const base::android::JavaRef<jobject>& obj,
    const base::android::JavaRef<jobject>& p0) __attribute__ ((unused));
static void Java_LinkedHashMap_forEach(JNIEnv* env, const base::android::JavaRef<jobject>& obj,
    const base::android::JavaRef<jobject>& p0) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env));

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "forEach",
          "(Ljava/util/function/BiConsumer;)V",
          &g_java_util_LinkedHashMap_forEach);

     env->CallVoidMethod(obj.obj(),
          call_context.base.method_id, p0.obj());
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_replaceAll(nullptr);
static void Java_LinkedHashMap_replaceAll(JNIEnv* env, const base::android::JavaRef<jobject>& obj,
    const base::android::JavaRef<jobject>& p0) __attribute__ ((unused));
static void Java_LinkedHashMap_replaceAll(JNIEnv* env, const base::android::JavaRef<jobject>& obj,
    const base::android::JavaRef<jobject>& p0) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, obj.obj(),
      java_util_LinkedHashMap_clazz(env));

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "replaceAll",
          "(Ljava/util/function/BiFunction;)V",
          &g_java_util_LinkedHashMap_replaceAll);

     env->CallVoidMethod(obj.obj(),
          call_context.base.method_id, p0.obj());
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_ConstructorJULIHM_I_F(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM_I_F(JNIEnv*
    env, JniIntWrapper p0,
    jfloat p1) __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM_I_F(JNIEnv*
    env, JniIntWrapper p0,
    jfloat p1) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, clazz,
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "<init>",
          "(IF)V",
          &g_java_util_LinkedHashMap_ConstructorJULIHM_I_F);

  jobject ret =
      env->NewObject(clazz,
          call_context.base.method_id, as_jint(p0), p1);
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_ConstructorJULIHM_I(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM_I(JNIEnv*
    env, JniIntWrapper p0) __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM_I(JNIEnv*
    env, JniIntWrapper p0) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, clazz,
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "<init>",
          "(I)V",
          &g_java_util_LinkedHashMap_ConstructorJULIHM_I);

  jobject ret =
      env->NewObject(clazz,
          call_context.base.method_id, as_jint(p0));
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_ConstructorJULIHM(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM(JNIEnv* env)
    __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM(JNIEnv* env)
    {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, clazz,
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "<init>",
          "()V",
          &g_java_util_LinkedHashMap_ConstructorJULIHM);

  jobject ret =
      env->NewObject(clazz,
          call_context.base.method_id);
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_ConstructorJULIHM_JUM(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM_JUM(JNIEnv*
    env, const base::android::JavaRef<jobject>& p0) __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM_JUM(JNIEnv*
    env, const base::android::JavaRef<jobject>& p0) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, clazz,
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "<init>",
          "(Ljava/util/Map;)V",
          &g_java_util_LinkedHashMap_ConstructorJULIHM_JUM);

  jobject ret =
      env->NewObject(clazz,
          call_context.base.method_id, p0.obj());
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

static std::atomic<jmethodID> g_java_util_LinkedHashMap_ConstructorJULIHM_I_F_Z(nullptr);
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM_I_F_Z(JNIEnv*
    env, JniIntWrapper p0,
    jfloat p1,
    jboolean p2) __attribute__ ((unused));
static base::android::ScopedJavaLocalRef<jobject> Java_LinkedHashMap_ConstructorJULIHM_I_F_Z(JNIEnv*
    env, JniIntWrapper p0,
    jfloat p1,
    jboolean p2) {
  jclass clazz = java_util_LinkedHashMap_clazz(env);
  CHECK_CLAZZ(env, clazz,
      java_util_LinkedHashMap_clazz(env), NULL);

  jni_generator::JniJavaCallContextChecked call_context;
  call_context.Init<
      base::android::MethodID::TYPE_INSTANCE>(
          env,
          clazz,
          "<init>",
          "(IFZ)V",
          &g_java_util_LinkedHashMap_ConstructorJULIHM_I_F_Z);

  jobject ret =
      env->NewObject(clazz,
          call_context.base.method_id, as_jint(p0), p1, p2);
  return base::android::ScopedJavaLocalRef<jobject>(env, ret);
}

}  // namespace JNI_LinkedHashMap

// Step 4: Generated test functions (optional).


#endif  // java_util_LinkedHashMap_JNI
