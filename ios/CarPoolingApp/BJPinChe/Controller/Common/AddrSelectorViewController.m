//
//  AddrSelectorViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-23.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  地址选择界面 通过代理方法将地址传递

#import "AddrSelectorViewController.h"
#import "AddrSelTableViewCell.h"
#import "AddrSelTableViewCell_new.h"
#import "BaiduSuggestionAdr.h"
@interface AddrSelectorViewController ()
@property(nonatomic,strong)NSMutableArray *selectedAdr;
@end


#define ADDRCELL_HEIGHT                     50


@implementation AddrSelectorViewController

@synthesize mParentTag;
@synthesize defText;



- (NSMutableArray *)selectedAdr
{
	if(_selectedAdr ==nil)
	{
		_selectedAdr =[[NSMutableArray alloc]init];
	}

	return _selectedAdr;
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.

	mAddrArray = [[NSMutableArray alloc]init];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userValue =[NSString stringWithFormat:@"%@",[defaults objectForKey:@"saveUser"]];
    NSString *userValue1 =[NSString stringWithFormat:@"%ld",[Common getUserID]];
    if ([userValue isEqualToString:userValue1]) {
        if ([_saveUser isEqualToString:@"1"])
        {
            NSMutableArray *array =[defaults objectForKey:@"addressArray"];
            for (int i=0 ; i < array.count ; i++)
            {
                NSDictionary *dic =[array objectAtIndex:i];
                BaiduSuggestionAdr* stInfo = [[BaiduSuggestionAdr alloc] initWithDict:dic];
                [mAddrArray addObject:stInfo];
            }
            if (mAddrArray.count !=0) {
                NSDictionary *dic = [array objectAtIndex:0];
                BaiduSuggestionAdr* stInfo = [[BaiduSuggestionAdr alloc] initWithDict:dic];
                [mAddrArray insertObject:stInfo atIndex:0];
            }
            btnHidden = @"1";
        }
    }else
    {
        NSString *user = [NSString stringWithFormat:@"%ld",[Common getUserID]];
        [defaults setObject:user forKey:@"saveUser"];
    }
    [defaults synchronize];
    
    
//    [dataList addObject:stInfo];
    

	_txtAddress.delegate = self;
    [_txtAddress becomeFirstResponder];

	_btn_remove.hidden = YES;
	_img_remove.hidden = YES;

	[_txtAddress addTarget:self action:@selector(onAddrChanged:) forControlEvents:UIControlEventEditingChanged];

	if (self.city != nil && self.city.length > 0) {
		NSString* lastChar = [self.city substringFromIndex:self.city.length - 1];
		if (![lastChar isEqualToString:@"市"]) {
			self.city = [self.city stringByAppendingString:@"市"];
		}
	}

	// check default text
    if ((defText != nil) && ([defText length] > 0)) {
        // set default text ( getting by voice recognizer )
        [_txtAddress setText:defText];
        if (self.city != NULL && self.city.length > 0 && [[_txtAddress text] length] > 0) {
            [self callSugggestionAddr:[_txtAddress text] region:self.city];
        }else if ([[_txtAddress text] length] > 0) {
            [self callSugggestionAddr:[_txtAddress text] region:[Common getCurrentCity]];
        }
    }
    
    [self updateUI];
    
}


- (void)onAddrChanged:(id)sender {
	_btn_remove.hidden = [_txtAddress.text isEqualToString:@""];
	_img_remove.hidden = [_txtAddress.text isEqualToString:@""];
    if (self.city != NULL && self.city.length > 0 && [[_txtAddress text] length] > 0) {
        // [self callSearchAddr:[textField text] region:self.city];
        [self callSugggestionAddr:[_txtAddress text] region:self.city];
    } else if ([[_txtAddress text] length] > 0) {
        //[self callSearchAddr:[textField text] region:[Common getCurrentCity]];
        [self callSugggestionAddr:[_txtAddress text] region:[Common getCurrentCity]];
    }
    if (_txtAddress.text.length == 0) {
        [mAddrArray removeAllObjects];
        if ([_saveUser isEqualToString:@"1"])
        {
            NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
            NSMutableArray *array =[defaults objectForKey:@"addressArray"];
            for (int i=0 ; i < array.count ; i++)
            {
                NSDictionary *dic =[array objectAtIndex:i];
                BaiduSuggestionAdr* stInfo = [[BaiduSuggestionAdr alloc] initWithDict:dic];
                [mAddrArray addObject:stInfo];
            }
            if (mAddrArray.count !=0) {
                NSDictionary *dic = [array objectAtIndex:0];
                BaiduSuggestionAdr* stInfo = [[BaiduSuggestionAdr alloc] initWithDict:dic];
                [mAddrArray insertObject:stInfo atIndex:0];
            }
            btnHidden = @"1";
        }
        [self updateUI];
    }
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [_txtAddress resignFirstResponder];
}

- (void) updateUI
{
    [_tableView reloadData];
}

- (void)searchAddr {
	if (self.city != NULL && self.city.length > 0 && [[_txtAddress text] length] > 0) {
		// [self callSearchAddr:[textField text] region:self.city];
		[self callSugggestionAddr:[_txtAddress text] region:self.city];
	} else if ([[_txtAddress text] length] > 0) {
		//[self callSearchAddr:[textField text] region:[Common getCurrentCity]];
		[self callSugggestionAddr:[_txtAddress text] region:[Common getCurrentCity]];
	}
	[_txtAddress resignFirstResponder];
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - UITextField Delegation
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
	[self searchAddr];
    return YES;
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string;{
//    if (_txtAddress == textField) {
//        if (self.city != NULL && self.city.length > 0 ) {
//            [self callSugggestionAddr:[textField text] region:self.city];
//        } else {
//            [self callSugggestionAddr:[textField text] region:[Common getCurrentCity]];
//        }
//    }
    return YES;
}

#pragma mark -百度地址suggestion

/**
 * call get all usual route  百度地址网络请求方法
 */
- (void) callSugggestionAddr : (NSString *)keyword region:(NSString *)region
{
//    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    //   BC2405e7fa0abaeda8f246d8f1b50923
    
    // Call the GetUsualRoutes service routine.
    [[CommManager getCommMgr] globalSvcMgr].delegate = self;
    [[[CommManager getCommMgr]globalSvcMgr]BaiduSuggestionAddr:keyword region:region apikey:@"BC2405e7fa0abaeda8f246d8f1b50923"];
}
#pragma 代理方法接收地址模型  baidu Suggestion
- (void) baiduSuggestionAddrResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:@"ok"])
    {
        [SVProgressHUD dismiss];
        
        [mAddrArray removeAllObjects];
        btnHidden = @"2";
        
        for (NSDictionary *dic in dataList) {
            BaiduSuggestionAdr * info = dic;
            if ([info.city isEqualToString:self.city] && self.city.length !=0) {
                [mAddrArray addObject:dic];
            }else if ([info.city isEqualToString:[Common getCurrentCity]])
            {
                [mAddrArray addObject:dic];
            }
        }
        if (mAddrArray.count ==0) {
            for (NSDictionary *dic in dataList) {
                BaiduSuggestionAdr * info = dic;
                [mAddrArray addObject:dic];
            }
        }
        // refresh table
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - 百度地址search  place api

/**
 * call get all usual route  百度地址网络请求方法
 */
- (void) callSearchAddr : (NSString *)keyword region:(NSString *)region
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    //TEST_NETWORK_RETURN;
    
    [self.selectedAdr removeAllObjects];
    // Call the GetUsualRoutes service routine.
    [[CommManager getCommMgr] globalSvcMgr].delegate = self;
    [[[CommManager getCommMgr] globalSvcMgr] BaiduSearchAddr:keyword region:region apikey:nil];
}
#pragma 代理方法接收地址模型
/**
 *  接收查询结果，关闭HUD 关闭当前页面
 *
 *  @param result   <#result description#>
 *  @param dataList <#dataList description#>
 */
- (void) baiduSearchAddrResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];

		self.selectedAdr = dataList;

		// refresh table
        //[self updateUI];
        [self dismissViewControllerAnimated:YES completion:^
         {
             [self.delegate onSelectedAddress:self.selectedAdr[0] parentTag:self.mParentTag];
         }];

    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Standard TableView delegates

////////////////////////////////////////////////////////////////////////////////////////////////////
- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (mAddrArray == nil) {
        return 0;
    }
    return mAddrArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//return height1;
	return ADDRCELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellIdentifier = @"addrcell";
    BaiduSuggestionAdr * info = [mAddrArray objectAtIndex:indexPath.row];
    
//    AddrSelTableViewCell *cell =[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
//    cell.selectionStyle = UITableViewCellSelectionStyleNone;
//    [cell initData:info];
#warning 测试修改
  // 自定义cell
    AddrSelTableViewCell_new * cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell==nil){
        cell=[[AddrSelTableViewCell_new alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    if ([btnHidden isEqualToString:@"1"]) {
        cell.delegate = self;
        if (indexPath.row == 0) {
            cell.btnHidden = btnHidden;
        }
        cell.mDataInfo =info;
    }else
    {
        cell.btnHidden = btnHidden;
        cell.mDataInfo =info;
    }
    
	return cell;
    
}
-(void)btnCellClick
{
    if ([btnHidden isEqualToString:@"1"]) {
        [mAddrArray removeAllObjects];
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults removeObjectForKey:@"addressArray"];
        btnHidden = @"2";
        [self updateUI];
    }
}
/**
 *  根据用户选择的suggestion 数据模型调用  search api
 *
 *  @param tableView <#tableView description#>
 *  @param indexPath indexPath description
 */
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([btnHidden isEqualToString:@"1"] && indexPath.row ==0) {
        [mAddrArray removeAllObjects];
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        [defaults removeObjectForKey:@"addressArray"];
        [self updateUI];
    }
    
	BaiduSuggestionAdr * info = [mAddrArray objectAtIndex:indexPath.row];
    
    if ([_saveUser isEqualToString:@"1"]) {
        NSMutableDictionary *dic =[NSMutableDictionary dictionary];
        [dic setObject:info.name forKey:@"name"];
        [dic setObject:info.city forKey:@"city"];
        [dic setObject:info.district forKey:@"district"];
        [dic setObject:info.business forKey:@"business"];
        [dic setObject:info.cityid forKey:@"cityid"];
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        
        NSMutableArray *array  =  nil;
        array = [defaults objectForKey:@"addressArray"];
        if (array == nil) {
            array =[[NSMutableArray alloc] init];
        }else
        {
            NSMutableArray *mutableArray = [[NSMutableArray alloc] init];
            for (NSDictionary *dic in array) {
                [mutableArray addObject:dic];
            }
            array = [NSMutableArray arrayWithArray:mutableArray];
        }
        for (int i = 0; i< array.count; i++)
        {
            NSDictionary *dic1 =[array objectAtIndex:i];
            if ([[dic1 objectForKey:@"name"] isEqualToString: [dic objectForKey:@"name"]])
            {
                [array removeObject:dic1];
            }
        }
        
        if (array.count ==5) {
            [array removeObjectAtIndex:4];
        }
        
        [array insertObject:dic atIndex:0];
        
        [defaults setObject:array forKey:@"addressArray"];
        [defaults synchronize];
    }

	// 精确查找
	if ([info.city isEqualToString:@""])
	{
		[self callSearchAddr :info.name  region:[Common getCurrentCity]];
	} else {
		[self callSearchAddr :info.name  region:info.city];
	}
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onClickSearch:(id)sender {
	[self searchAddr];
}


- (IBAction)onClickClear:(id)sender {
	_txtAddress.text = @"";
	_btn_remove.hidden = YES;
	_img_remove.hidden = YES;
    if (_txtAddress.text.length == 0) {
        [mAddrArray removeAllObjects];
        if ([_saveUser isEqualToString:@"1"])
        {
            NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
            NSMutableArray *array =[defaults objectForKey:@"addressArray"];
            for (int i=0 ; i < array.count ; i++)
            {
                NSDictionary *dic =[array objectAtIndex:i];
                BaiduSuggestionAdr* stInfo = [[BaiduSuggestionAdr alloc] initWithDict:dic];
                [mAddrArray addObject:stInfo];
            }
            if (mAddrArray.count !=0) {
                NSDictionary *dic = [array objectAtIndex:0];
                BaiduSuggestionAdr* stInfo = [[BaiduSuggestionAdr alloc] initWithDict:dic];
                [mAddrArray insertObject:stInfo atIndex:0];
            }
            btnHidden = @"1";
        }
    }
    [self updateUI];
}

@end
