//
//  CommManager.m
//  FourService
//
//  Created by RyuCJ on 24/10/2012.
//  Copyright (c) 2012 PIC. All rights reserved.
//

#import <sys/socket.h>
#import <netinet/in.h>
#import <SystemConfiguration/SystemConfiguration.h>
#import "CommManager.h"
#import "Common.h"
//#import "SBJson.h"
#import "ServiceMethod.h"
#import "AFHTTPClient.h"


//////////////////////////////////////////////////////////////////////
#pragma mark - Communication Manager implementation

@implementation CommManager

@synthesize bathServiceUrl;
@synthesize mainSvcMgr;
@synthesize accountSvcMgr;
@synthesize tradeSvcMgr;

@synthesize commMgr;

static CommManager *commMgr = nil;

+ (CommManager *)getCommMgr
{    
    if (commMgr == nil) {        
        commMgr = [[CommManager alloc] init];
        [commMgr loadCommModules];
    }
    
    return commMgr;
}

/*
 Connectivity testing code pulled from Apple's Reachability Example :http://developer.apple.com/library/ios/#samplecode/Reachability
 */
+ (BOOL)hasConnectivity
{
    struct sockaddr_in zeroAddress;
    bzero(&zeroAddress, sizeof(zeroAddress));
    zeroAddress.sin_len = sizeof(zeroAddress);
    zeroAddress.sin_family = AF_INET;
    
    SCNetworkReachabilityRef reachability = SCNetworkReachabilityCreateWithAddress(kCFAllocatorDefault, (const struct sockaddr*)&zeroAddress);
    if(reachability != NULL) {
        //NetworkStatus retVal = NotReachable;
        SCNetworkReachabilityFlags flags;
        if (SCNetworkReachabilityGetFlags(reachability, &flags)) {
            if ((flags & kSCNetworkReachabilityFlagsReachable) == 0)
            {
                // if target host is not reachable
                return NO;
            }
            
            if ((flags & kSCNetworkReachabilityFlagsConnectionRequired) == 0)
            {
                // if target host is reachable and no connection is required
                //  then we'll assume (for now) that your on Wi-Fi
                return YES;
            }
            
            
            if ((((flags & kSCNetworkReachabilityFlagsConnectionOnDemand ) != 0) ||
                 (flags & kSCNetworkReachabilityFlagsConnectionOnTraffic) != 0))
            {
                // ... and the connection is on-demand (or on-traffic) if the
                //     calling application is using the CFSocketStream or higher APIs
                
                if ((flags & kSCNetworkReachabilityFlagsInterventionRequired) == 0)
                {
                    // ... and no [user] intervention is needed
                    return YES;
                }
            }
            
            if ((flags & kSCNetworkReachabilityFlagsIsWWAN) == kSCNetworkReachabilityFlagsIsWWAN)
            {
                // ... but WWAN connections are OK if the calling application
                //     is using the CFNetwork (CFSocketStream?) APIs.
                return YES;
            }
        }
    }
    
    return NO;
}

- (void)loadCommModules
{     
	mainSvcMgr = [[MainSvcMgr alloc] init];
    accountSvcMgr = [[AccountSvcMgr alloc] init];
    tradeSvcMgr = [[TradeSvcMgr alloc] init];
    
    bathServiceUrl = [ServiceMethod getBaseServiceAddress];
}

@end