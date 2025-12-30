//
//  Common.m
//  4S-C
//
//  Created by R CJ on 1/5/13.
//  Copyright (c) 2013 PIC. All rights reserved.
//

#import "Common.h"
#import <CommonCrypto/CommonDigest.h>

// Common variables

STUserInfo *    _userInfo = nil;

NSString *      _deviceToken = @"";
NSString *      _curCity = @"";
NSString *      _verifyState = @"";

#define _MAXLENGTH       50

#define SYSTEM_VERSION_EQUAL_TO(v)                  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedSame)
#define SYSTEM_VERSION_GREATER_THAN(v)              ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedDescending)
#define SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)
#define SYSTEM_VERSION_LESS_THAN(v)                 ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedAscending)
#define SYSTEM_VERSION_LESS_THAN_OR_EQUAL_TO(v)     ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedDescending)

@implementation Common

+ (BOOL) isIOSVer7
{
    if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"7.0")) {
        // code here
        return true;
    }
    
    return false;
}

+ (float)getSystemVersion
{
	return [[[UIDevice currentDevice] systemVersion] floatValue];
}

+ (void) makeErrorWindow : (NSString *)content TopOffset:(NSInteger)topOffset BottomOffset:(NSInteger)bottomOffset View:(UIView *)view
{
    CGRect rt = [view frame];
    UIImageView * imgView = [[UIImageView alloc] initWithFrame:CGRectMake(0., topOffset, rt.size.width, rt.size.height - topOffset - bottomOffset)];
    [imgView setImage:[UIImage imageNamed:@"bkError.png"]];
    
    UILabel * lblContent = [[UILabel alloc] initWithFrame:CGRectMake(0., topOffset, rt.size.width, rt.size.height - topOffset - bottomOffset)];
    lblContent.backgroundColor = [UIColor clearColor];
    lblContent.textAlignment = UITextAlignmentCenter;
    lblContent.text = content;
    
    [view addSubview:imgView];
    [view addSubview:lblContent];
}


+ (void) setDeviceToken : (NSString*)newDeviceToken
{
    _deviceToken = newDeviceToken;
}

+ (NSString*) deviceToken
{
    return _deviceToken;
}


+ (NSInteger) MAXLENGTH
{
    return _MAXLENGTH;
}

+ (NSString*) getCurTime : (NSString*)fmt
{
    NSDate *currentDate = [NSDate date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    
    if ( fmt == nil ) {
        [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    } else {
        [dateFormatter setDateFormat:fmt];
    }
    
    return [dateFormatter stringFromDate:currentDate];
}

+ (NSInteger)phoneType
{
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        if ([UIScreen mainScreen].bounds.size.height == 568) {
            return IPHONE5;
        }
        else {
            return IPHONE4;
        }
    }
    else {
        return IPAD;
    }
}


+ (NSString *)getRealImagePath :(NSString *)path :(NSString *)rate :(NSString *)size
{
    if (path.length > 0) {
        NSArray *pathArray = [path componentsSeparatedByString:@"/"];
        NSMutableString *realPath = [NSMutableString string];
        
        for (int i = 0; i < pathArray.count-1; i++) {
            [realPath appendString:[pathArray objectAtIndex:i]];
            [realPath appendString:@"/"];
        }
        
        [realPath appendString:@"640960"];
        [realPath appendString:@"_"];
        [realPath appendString:rate];
        [realPath appendString:@"_"];
        [realPath appendString:size];
        [realPath appendString:@"_"];
        [realPath appendString:[pathArray objectAtIndex:pathArray.count-1]];
        
        NSLog(@"%@", realPath);
        return realPath;
    }
    else {
        return @"";
    }
}

+ (NSString *)getBackImagePath :(NSString *)path :(NSString *)rate :(NSString *)size
{
    if (path.length > 0) {
        NSArray *pathArray = [path componentsSeparatedByString:@"/"];
        NSMutableString *realPath = [NSMutableString string];
        
        for (int i = 0; i < pathArray.count-1; i++) {
            [realPath appendString:[pathArray objectAtIndex:i]];
            [realPath appendString:@"/"];
        }
        
        if ([Common phoneType] == IPHONE5) {
            [realPath appendString:@"6401136"];
        }
        else {
            [realPath appendString:@"640960"];
        }
        [realPath appendString:@"_"];
        [realPath appendString:rate];
        [realPath appendString:@"_"];
        [realPath appendString:size];
        [realPath appendString:@"_"];
        [realPath appendString:[pathArray objectAtIndex:pathArray.count-1]];
        
        NSLog(@"%@", realPath);
        return realPath;
    }
    else {
        return @"";
    }
}

+ (NSString*)base64forData:(NSData*)theData
{
    const uint8_t* input = (const uint8_t*)[theData bytes];
    NSInteger length = [theData length];
    
    static char table[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    
    NSMutableData* data = [NSMutableData dataWithLength:((length + 2) / 3) * 4];
    uint8_t* output = (uint8_t*)data.mutableBytes;
    
    NSInteger i;
    for (i=0; i < length; i += 3) {
        NSInteger value = 0;
        NSInteger j;
        for (j = i; j < (i + 3); j++) {
            value <<= 8;
            
            if (j < length) {
                value |= (0xFF & input[j]);
            }
        }
        
        NSInteger theIndex = (i / 3) * 4;
        output[theIndex + 0] =                    table[(value >> 18) & 0x3F];
        output[theIndex + 1] =                    table[(value >> 12) & 0x3F];
        output[theIndex + 2] = (i + 1) < length ? table[(value >> 6)  & 0x3F]  :'=';
        output[theIndex + 3] = (i + 2) < length ? table[(value >> 0)  & 0x3F]  :'=';
    }
    
    return [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
}

+ (NSData*)base64forString:(NSString*)theString
{
    NSMutableData *mutableData = nil;

    if (theString) {
		unsigned long ixtext = 0;
		unsigned long lentext = 0;
		unsigned char ch = 0;
		unsigned char inbuf[4], outbuf[3];
		short i = 0, ixinbuf = 0;
		BOOL flignore = NO;
		BOOL flendtext = NO;
		NSData *base64Data = nil;
		const unsigned char *base64Bytes = nil;
        
		// Convert the string to ASCII data.
		base64Data = [theString dataUsingEncoding:NSASCIIStringEncoding];
		base64Bytes = [base64Data bytes];
		mutableData = [NSMutableData dataWithCapacity:[base64Data length]];
		lentext = [base64Data length];
        
		while( YES ) {
			if( ixtext >= lentext ) break;
			ch = base64Bytes[ixtext++];
			flignore = NO;
            
			if( ( ch >= 'A' ) && ( ch <= 'Z' ) ) ch = ch - 'A';
			else if( ( ch >= 'a' ) && ( ch <= 'z' ) ) ch = ch - 'a' + 26;
			else if( ( ch >= '0' ) && ( ch <= '9' ) ) ch = ch - '0' + 52;
			else if( ch == '+' ) ch = 62;
			else if( ch == '=' ) flendtext = YES;
			else if( ch == '/' ) ch = 63;
			else flignore = YES;
            
			if( ! flignore ) {
				short ctcharsinbuf = 3;
				BOOL flbreak = NO;
                
				if( flendtext ) {
					if( ! ixinbuf ) break;
					if( ( ixinbuf == 1 ) || ( ixinbuf == 2 ) ) ctcharsinbuf = 1;
					else ctcharsinbuf = 2;
					ixinbuf = 3;
					flbreak = YES;
				}
                
				inbuf [ixinbuf++] = ch;
                
				if( ixinbuf == 4 ) {
					ixinbuf = 0;
					outbuf [0] = ( inbuf[0] << 2 ) | ( ( inbuf[1] & 0x30) >> 4 );
					outbuf [1] = ( ( inbuf[1] & 0x0F ) << 4 ) | ( ( inbuf[2] & 0x3C ) >> 2 );
					outbuf [2] = ( ( inbuf[2] & 0x03 ) << 6 ) | ( inbuf[3] & 0x3F );
                    
					for( i = 0; i < ctcharsinbuf; i++ )
						[mutableData appendBytes:&outbuf[i] length:1];
				}
                
				if( flbreak )  break;
			}
		}
	}
    
	return mutableData;
}


+ (NSString *)appNameAndVersionNumberDisplayString 
{
    NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
    //NSString *appDisplayName = [infoDictionary objectForKey:@"CFBundleDisplayName"];
    NSString *majorVersion = [infoDictionary objectForKey:@"CFBundleShortVersionString"];
    //NSString *minorVersion = [infoDictionary objectForKey:@"CFBundleVersion"];
    
    return majorVersion;
}

+ (NSString *) md5:(NSString *) input
{
    const char *cStr = [input UTF8String];
    unsigned char digest[16];
    CC_MD5( cStr, strlen(cStr), digest ); // This is the md5 call
    
    NSMutableString *output = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
    
    for(int i = 0; i < CC_MD5_DIGEST_LENGTH; i++)
        [output appendFormat:@"%02x", digest[i]];
    
    return  output;
    
}


+ (NSString*)getAdvertiseIdentifier
{
    
	NSUUID* uuid = [[ASIdentifierManager sharedManager] advertisingIdentifier];
	if (uuid == nil)
		return @"";
    
	return [uuid UUIDString];
}

+ (NSString*)getDeviceIDForVendor
{
	return [[UIDevice currentDevice].identifierForVendor UUIDString];
}


+ (NSString*)getDeviceMacAddress 
{
	if ([Common getSystemVersion] >= 7)
		return [Common getAdvertiseIdentifier];
	else
		return [[UIDevice currentDevice] MACAddress];
}


+ (int)validateName:(NSString*)name allowChinese:(BOOL)allowChinese
{
	if (name == nil || [name isEqualToString:@""])
		return -1;


	for (int i = 0; i < name.length; i++)
	{
		unichar chItem = [name characterAtIndex:i];

		if (chItem == ' ') {
			return -2;
		}

		if (i == 0)
		{
			if (![[NSCharacterSet uppercaseLetterCharacterSet] characterIsMember:chItem] &&
				![[NSCharacterSet lowercaseLetterCharacterSet] characterIsMember:chItem] &&
				![Common isChinese:chItem])
			{
				return -3;
			}
		}
		else
		{
			if (![[NSCharacterSet uppercaseLetterCharacterSet] characterIsMember:chItem] &&
				![[NSCharacterSet lowercaseLetterCharacterSet] characterIsMember:chItem] &&
				![Common isChinese:chItem] &&
				![[NSCharacterSet decimalDigitCharacterSet] characterIsMember:chItem])
			{
				return -1;
			}

			if (!allowChinese && [Common isChinese:chItem])
				return -1;
		}
	}

	return 0;
}



+ (BOOL)isChinese:(unichar)chItem
{
	if (chItem > 0x400 && chItem < 0x9fff)
		return YES;
    
	return NO;
}


+ (int)getIntValueWithKey:(NSString*)key defaultValue:(int)defaultValue Dict:(NSDictionary*)dict
{
	long value = [self getLongValueWithKey:key defaultValue:defaultValue Dict:dict];
	return (int)value;
}

+ (long)getLongValueWithKey:(NSString*)key defaultValue:(long)defaultValue Dict:(NSDictionary*)dict
{
	id objValue = [dict objectForKey:key];
	if (objValue == nil)
		return defaultValue;
	
	return (long)[objValue longLongValue];
}

+ (NSString*)getStringValueWithKey:(NSString*)key defaultValue:(NSString*)defaultValue Dict:(NSDictionary*)dict
{
	id objValue = [dict objectForKey:key];
	if (objValue == nil)
		return defaultValue;
	
	return (NSString*)objValue;
}

+ (double)getDoubleValueWithKey:(NSString*)key defaultValue:(double)defaultValue Dict:(NSDictionary*)dict
{
	id objValue = [dict objectForKey:key];
	if (objValue == nil)
		return defaultValue;
	
	return [objValue doubleValue];
}

+ (float)getFloatValueWithKey:(NSString*)key defaultValue:(float)defaultValue Dict:(NSDictionary*)dict
{
	id objValue = [dict objectForKey:key];
	if (objValue == nil)
		return defaultValue;
	
	return [objValue floatValue];
}


+ (void) setUserInfo : (STUserInfo *)info
{
    _userInfo = info;
}

+ (STUserInfo *) getUserInfo
{
    return _userInfo;
}

+ (long) getUserID
{
    if (_userInfo == nil) {
        return 0;
    }
    
    return [_userInfo userid];
}

+ (void) setCurrentCity : (NSString *)curCity
{
    _curCity = curCity;
}

+ (NSString *) getCurrentCity
{
    return _curCity;
}

+ (void) setVerifyState : (NSString *)state
{
    _verifyState = state;
}

+ (NSString *) getVerifyState
{
    if([_verifyState isEqualToString:@""])
    {
        return @"0";
    }else
    {
        return _verifyState;
    }
}

+ (void) logout
{
    UIStoryboard *st = [UIStoryboard storyboardWithName:[[NSBundle mainBundle].infoDictionary objectForKey:@"UIMainStoryboardFile"] bundle:[NSBundle mainBundle]];
    
    UIWindow * window = [[UIApplication sharedApplication] keyWindow];
    UIViewController * srcCtrl = window.rootViewController;
    
    // go to main view controller
    UIViewController * destCtrl = [st instantiateViewControllerWithIdentifier:@"maintab"];
    
    [srcCtrl presentViewController:destCtrl animated:YES completion:nil];
}

@end
