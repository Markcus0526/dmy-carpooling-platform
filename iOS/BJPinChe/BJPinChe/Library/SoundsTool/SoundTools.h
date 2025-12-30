//
//  SoundTools.h
//  音效播放
//
//  Created by maguanghai on 14-7-29.
//  Copyright (c) 2014年 maguanghai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Singleton.h"
@interface SoundTools : NSObject


singletonInterface(SoundTools)

-(void)playSoundWithFileName:(NSString *)fileName;
@end
