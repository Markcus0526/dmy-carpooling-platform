//
//  Common.h
//  4S-C
//
//  Created by R CJ on 1/5/13.
//  Copyright (c) 2013 PIC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import <MapKit/MapKit.h>
#import "STDataInfo.h"
#import "RewindFromRight.h"
#import <ifaddrs.h>
#import <arpa/inet.h>
#import <AdSupport/AdSupport.h>
#import "MACAddress.h"



#define DEF_DELAY                       2
#define DEF_PAGECOUNT                   10


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

#define TEST_NETWORK_RETURN		if ([CommManager hasConnectivity] == NO) { \
									[DejalActivityView removeView]; \
									if ([SVProgressHUD isVisible]) \
										[SVProgressHUD dismissWithError:@"网络不给力，稍后再试吧"]; \
									else \
										[SVProgressHUD showErrorWithStatus:@"网络不给力，稍后再试吧"]; \
									return; \
								}


#define BACKGROUND_TEST_NETWORK_RETURN    if ([CommManager hasConnectivity] == NO) { \
                                                return; \
                                            }


#define MYCOLOR_GREEN         [UIColor colorWithRed:52.0/255.0 green:135.0/255.0 blue:91.0/255.0 alpha:1.0]

// Added by KHM. 2014.11.21
#define VERIFYKEY_VALIDPERIOD					5 * 60							// 300 seconds(5 minutes).
#define VERIFYKEY_INTERVAL						1 * 180							// 60 seconds(1 minutes).
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

+ (double) calcDistOfCoords : (CLLocationCoordinate2D)coord1 coord2:(CLLocationCoordinate2D)coord2;

/************************************************/
+ (void) setUserInfo : (STUserInfo *)info;
+ (STUserInfo *) getUserInfo;

+ (long) getUserID;

+ (void) setCurrentPosLat:(double)lat andLng:(double)lng;
+ (double) getCurrentLatitude;
+ (double) getCurrentLongitude;

+ (void) setCurrentClientSide:(int)side;
+ (int) getCurrentClientSide;

+ (void) setCurrentCity : (NSString *)curCity;
+ (NSString *) getCurrentCity;
+ (void)setCurrentAdress:(NSString *)curAdr;
+ (NSString *)getCurrentAdress;
+ (void) setVerifyState : (NSString *)state;
+ (NSString *) getVerifyState;

+ (void) duplicateLogout : (UIViewController *)srcCtrl;
+ (void) setCarVerifyingInfo : (STCarVerifyingInfo *)info;
+ (STCarVerifyingInfo *) getCarVerifyingInfo;
+ (void)saveImage:(UIImage*)image toLocalFile:(NSString*)path;
+ (UIImage*)loadImageFromLocalFile:(NSString*)path;
+ (UIImage*)scaleImage:(UIImage*)image toSize:(CGSize)size;



+ (BOOL)validateName:(NSString*)name allowChinese:(BOOL)allowChinese;


+ (int)getIntValueWithKey:(NSString*)key defaultValue:(int)defaultValue Dict:(NSDictionary*)dict;
+ (long)getLongValueWithKey:(NSString*)key defaultValue:(long)defaultValue Dict:(NSDictionary*)dict;
+ (NSString*)getStringValueWithKey:(NSString*)key defaultValue:(NSString*)defaultValue Dict:(NSDictionary*)dict;
+ (double)getDoubleValueWithKey:(NSString*)key defaultValue:(double)defaultValue Dict:(NSDictionary*)dict;
+ (float)getFloatValueWithKey:(NSString*)key defaultValue:(float)defaultValue Dict:(NSDictionary*)dict;


+(void)setBaiDuPushUserID:(NSString *)pushUserID;
+(NSString *)baiduPushUserID;

+(void)setBaiDuPushChannelID:(NSString *)channelID;
+(NSString *)baiduPushChannelID;


+ (NSString*)getConcealedCarNo:(NSString*)carno;



@end
