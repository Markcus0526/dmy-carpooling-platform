//
//  Common.h
//  4S-C
//
//  Created by R CJ on 1/5/13.
//  Copyright (c) 2013 PIC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "STDataInfo.h"
#import "RewindFromRight.h"
#import <ifaddrs.h>
#import <arpa/inet.h>
#import <AdSupport/AdSupport.h>
#import "MACAddress.h"



#define MOVE_FROM_RIGHT     CATransition *animation = [CATransition animation]; \
                            [animation setDuration:0.3]; \
                            [animation setType:kCATransitionPush]; \
                            [animation setSubtype:kCATransitionFromRight]; \
                            [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
                            [[self.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];

#define MOVE_FROM_LEFT      CATransition *animation = [CATransition animation]; \
                            [animation setDuration:0.3]; \
                            [animation setType:kCATransitionPush]; \
                            [animation setSubtype:kCATransitionFromLeft]; \
                            [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
                            [[self.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];

#define SHOW_VIEW(ctrl)     MOVE_FROM_RIGHT \
                            [self presentViewController:ctrl animated:NO completion:nil];

#define SHOW_VIEW_IN_CELL(parent, ctrl)  \
                            CATransition *animation = [CATransition animation]; \
                            [animation setDuration:0.3]; \
                            [animation setType:kCATransitionPush]; \
                            [animation setSubtype:kCATransitionFromRight]; \
                            [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
                            [[parent.view.superview layer] addAnimation:animation forKey:@"SwitchToView"]; \
                            [parent presentViewController:ctrl animated:NO completion:nil];


#define BACK_VIEW           MOVE_FROM_LEFT \
                            [self dismissViewControllerAnimated:NO completion:nil];

#define TEST_NETWORK_RETURN if ([CommManager hasConnectivity] == NO) { \
                                [DejalActivityView removeView]; \
                                [SVProgressHUD dismissWithError:@"没有网络连接"]; \
                                return; \
                            }

#define BACKGROUND_TEST_NETWORK_RETURN    if ([CommManager hasConnectivity] == NO) { \
                                                return; \
                                            }

#define SHOW_MSG(message)		[self.view makeToast:message duration:DEF_DELAY position:@"center"];

#define MYCOLOR_GREEN         [UIColor colorWithRed:52.0/255.0 green:135.0/255.0 blue:91.0/255.0 alpha:1.0]


#define VERIFYKEY_VALIDPERIOD		5 * 60 + 1							// 300 seconds(5 minutes).
#define VERIFYKEY_INTERVAL			1 * 60 + 1							// 60 seconds(1 minutes).
#define GBK_ENCODING			CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000)	// GBK Encoding


typedef NS_ENUM(NSInteger, DEVICE_KIND) {
    IPHONE4= 1,
    IPHONE5,
    IPAD,
};

@interface Common : NSObject {
}

+ (BOOL) isIOSVer7;
+ (float)getSystemVersion;

+ (void) makeErrorWindow : (NSString *)content TopOffset:(NSInteger)topOffset BottomOffset:(NSInteger)bottomOffset View:(UIView *)view;

+ (void) setDeviceToken : (NSString*)newDeviceToken;
+ (NSString*) deviceToken;

+ (NSInteger) MAXLENGTH;

+ (NSString*) getCurTime : (NSString*)fmt;

+ (NSInteger) phoneType;

+ (NSString *) getRealImagePath :(NSString *)path :(NSString *)rate :(NSString *)size;
+ (NSString *) getBackImagePath :(NSString *)path :(NSString *)rate :(NSString *)size;

+ (NSString*) base64forData:(NSData*)theData;
+ (NSData*) base64forString:(NSString*)theString;

+ (NSString *) appNameAndVersionNumberDisplayString;

+ (NSString *) md5:(NSString *) input;

+ (NSString*)getAdvertiseIdentifier;
+ (NSString*)getDeviceIDForVendor;
+ (NSString*)getDeviceMacAddress;       // used for IMEI

+ (int)validateName:(NSString*)name allowChinese:(BOOL)allowChinese;


+ (int)getIntValueWithKey:(NSString*)key defaultValue:(int)defaultValue Dict:(NSDictionary*)dict;
+ (long)getLongValueWithKey:(NSString*)key defaultValue:(long)defaultValue Dict:(NSDictionary*)dict;
+ (NSString*)getStringValueWithKey:(NSString*)key defaultValue:(NSString*)defaultValue Dict:(NSDictionary*)dict;
+ (double)getDoubleValueWithKey:(NSString*)key defaultValue:(double)defaultValue Dict:(NSDictionary*)dict;
+ (float)getFloatValueWithKey:(NSString*)key defaultValue:(float)defaultValue Dict:(NSDictionary*)dict;




/************************************************/
+ (void) setUserInfo : (STUserInfo *)info;
+ (STUserInfo *) getUserInfo;

+ (long) getUserID;

+ (void) setCurrentCity : (NSString *)curCity;
+ (NSString *) getCurrentCity;
+ (void) setVerifyState : (NSString *)state;
+ (NSString *) getVerifyState;




+ (void) logout;

@end
